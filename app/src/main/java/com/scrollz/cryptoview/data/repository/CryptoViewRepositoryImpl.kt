package com.scrollz.cryptoview.data.repository

import androidx.room.withTransaction
import com.scrollz.cryptoview.data.local.CryptoViewDataBase
import com.scrollz.cryptoview.data.remote.CoinApi
import com.scrollz.cryptoview.data.remote.CoinPaprikaApi
import com.scrollz.cryptoview.data.remote.TimeApi
import com.scrollz.cryptoview.domain.model.Coin
import com.scrollz.cryptoview.domain.model.DeferredNotification
import com.scrollz.cryptoview.domain.model.FavoriteCoin
import com.scrollz.cryptoview.domain.model.HistoricalTicks
import com.scrollz.cryptoview.domain.model.Notification
import com.scrollz.cryptoview.domain.repository.CryptoViewRepository
import com.scrollz.cryptoview.utils.Interval
import com.scrollz.cryptoview.utils.Period
import com.scrollz.cryptoview.utils.createDetailedCoin
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
    private val coinApi: CoinApi,
    private val timeApi: TimeApi,
    private val db: CryptoViewDataBase
) : CryptoViewRepository {

    private val dao = db.cryptoViewDao()

    override fun getCoins() = networkBoundResource(
        query = {
            dao.getCoinsList()
        },
        fetch = {
            val icons = coinApi.getIcons()
            coinPaprikaApi.getCoins().map { coinDto ->
                coinDto.toCoin(
                    iconUrl = icons.find {
                        it.assetId.uppercase() == coinDto.symbol.uppercase()
                    }?.url
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
                    day = list.filter { it.period == Period.DAY.value },
                    year = list.filter { it.period == Period.YEAR.value }
                )
            }
        },
        fetch = {
            val currentTime = timeApi.getCurrentTime().dateTime
            val dayTicks = coinPaprikaApi.getHistoricalTicks(
                id = id,
                startDateTime = timeForDayTicks(currentTime),
                interval = Interval.ONE_HOUR.value
            ).map { it.toTick(id, Period.DAY.value) }
            val yearTicks = coinPaprikaApi.getHistoricalTicks(
                id = id,
                startDateTime = timeForYearTicks(currentTime),
                interval = Interval.ONE_DAY.value
            ).map { it.toTick(id, Period.YEAR.value) }
            dayTicks.plus(yearTicks)
        },
        saveFetchResult = { ticks ->
            db.withTransaction {
                dao.deleteHistoricalTicks(id)
                dao.insertHistoricalTicks(ticks)
            }
        }
    )

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

    override suspend fun getNotifications(): List<Notification> {
        return dao.getNotifications()
    }

    override suspend fun addNotification(notification: Notification) {
        dao.upsertNotification(notification)
    }

    override suspend fun deleteNotification(coinID: String) {
        dao.deleteNotification(coinID)
    }

    override fun isNotificationOn(id: String): Flow<Boolean> {
        return dao.isNotificationOn(id)
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

}
