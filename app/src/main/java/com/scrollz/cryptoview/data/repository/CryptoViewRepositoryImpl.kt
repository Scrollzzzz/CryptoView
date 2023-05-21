package com.scrollz.cryptoview.data.repository

import android.util.Log
import androidx.room.withTransaction
import com.scrollz.cryptoview.data.local.CryptoViewDataBase
import com.scrollz.cryptoview.data.remote.CoinMarketCapApi
import com.scrollz.cryptoview.data.remote.CoinPaprikaApi
import com.scrollz.cryptoview.data.remote.TimeApi
import com.scrollz.cryptoview.domain.model.Coin
import com.scrollz.cryptoview.domain.model.DeferredNotification
import com.scrollz.cryptoview.domain.model.FavoriteCoin
import com.scrollz.cryptoview.domain.model.HistoricalTicks
import com.scrollz.cryptoview.domain.model.Icon
import com.scrollz.cryptoview.domain.model.Notification
import com.scrollz.cryptoview.domain.repository.CryptoViewRepository
import com.scrollz.cryptoview.utils.Interval
import com.scrollz.cryptoview.utils.Period
import com.scrollz.cryptoview.utils.createDetailedCoin
import com.scrollz.cryptoview.utils.ids
import com.scrollz.cryptoview.utils.networkBoundResource
import com.scrollz.cryptoview.utils.timeForDayTicks
import com.scrollz.cryptoview.utils.timeForYearTicks
import com.scrollz.cryptoview.utils.toCoin
import com.scrollz.cryptoview.utils.toTick
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CryptoViewRepositoryImpl @Inject constructor(
    private val coinPaprikaApi: CoinPaprikaApi,
    private val coinMarketCapApi: CoinMarketCapApi,
    private val timeApi: TimeApi,
    private val db: CryptoViewDataBase
) : CryptoViewRepository {

    private val dao = db.cryptoViewDao()

    override fun getCoins() = networkBoundResource(
        query = {
            dao.getCoinsList()
        },
        fetch = {
            val icons = dao.getCoinIcons()
            coinPaprikaApi.getCoins().map { coinDto ->
                coinDto.toCoin(
                    iconUrl = icons.find {
                        it.symbol == coinDto.symbol
                    }?.iconUrl
                )
            }
        },
        saveFetchResult = { coins ->
            db.withTransaction {
                dao.deleteAllCoins()
                dao.insertCoins(coins)
            }
        },
        shouldFetch = { true }
    )

    override suspend fun updateCoinIcons(coins: List<Coin>) {
        val icons = mutableListOf<Icon>()
        coins.ids().forEach { ids ->
            try {
                coinMarketCapApi.getIcons(ids).data.forEach { (_, icon) ->
                    if (icon.isNotEmpty()) icons.add(icon.first())
                }
            } catch (e: Exception) {
                Log.e("CMCIcons", e.message ?: "err")
            }
        }
        db.withTransaction {
            icons.forEach { icon ->
                dao.updateCoinIcons(icon.symbol, icon.iconUrl)
            }
        }
    }

    override fun getDetailedCoin(id: String) = networkBoundResource(
        query = {
            dao.getDetailedCoin(id)
        },
        fetch = {
            val coin = coinPaprikaApi.getCoin(id)
            val coinInfo = coinPaprikaApi.getCoinInfo(id)
            createDetailedCoin(coin, coinInfo)
        },
        saveFetchResult = { coin ->
            dao.upsertDetailedCoin(coin)
        }
    )

    override fun getHistoricalTicks(id: String) = networkBoundResource(
        query = {
            dao.getHistoricalTicks(id).map { list ->
                HistoricalTicks(
                    day = list.filter { it.period == Period.DAY },
                    year = list.filter { it.period == Period.YEAR }
                )
            }
        },
        fetch = {
            val currentTime = timeApi.getCurrentTime().dateTime
            val dayTicks = coinPaprikaApi.getHistoricalTicks(
                id = id,
                startDateTime = currentTime.timeForDayTicks(),
                interval = Interval.ONE_HOUR
            ).map { it.toTick(id, Period.DAY) }
            val yearTicks = coinPaprikaApi.getHistoricalTicks(
                id = id,
                startDateTime = currentTime.timeForYearTicks(),
                interval = Interval.ONE_DAY
            ).map { it.toTick(id, Period.YEAR) }
            dayTicks.plus(yearTicks)
        },
        saveFetchResult = { ticks ->
            db.withTransaction {
                dao.deleteHistoricalTicks(id)
                dao.insertHistoricalTicks(ticks)
            }
        }
    )

    override suspend fun getCoinIconsURLs(): List<String?> {
        return dao.getCoinIconsURLs()
    }

    override suspend fun getCoin(id: String): Coin {
        return coinPaprikaApi.getCoin(id).toCoin(null)
    }

    override fun getFavorites(): Flow<List<String>> {
        return dao.getFavorites()
    }

    override fun isCoinFavorite(id: String): Flow<Boolean> {
        return dao.isCoinFavorite(id)
    }

    override suspend fun toggleFavorite(id: String) {
        if (dao.isCoinFavorite(id).first()) {
            dao.deleteFromFavorites(id)
        }
        else {
            dao.insertIntoFavorites(FavoriteCoin(id))
        }
    }

    override fun getNotification(coinID: String): Flow<Notification?> {
        return dao.getNotification(coinID)
    }

    override suspend fun addNotification(notification: Notification) {
        dao.upsertNotification(notification)
    }

    override suspend fun deleteNotification(coinID: String) {
        dao.deleteNotification(coinID)
        dao.deleteDeferredNotification(coinID)
    }

    override suspend fun getDeferredNotificationsDeleting(): List<String> {
        var deferredNotifications = emptyList<String>()
        db.withTransaction {
            deferredNotifications = dao.getDeferredNotifications()
            dao.deleteAllDeferredNotifications()
        }
        return deferredNotifications
    }

    override suspend fun addDeferredNotificationEmptinessChecking(
        deferredNotification: DeferredNotification
    ): Boolean {
        var isEmpty = true
        db.withTransaction {
            isEmpty = dao.noDeferredNotifications()
            dao.upsertDeferredNotification(deferredNotification)
        }
        return isEmpty
    }

    override suspend fun getCurrentTime(): String {
        return timeApi.getCurrentTime().dateTime
    }

}
