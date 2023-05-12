package com.scrollz.cryptoview.data.repository

import androidx.room.withTransaction
import com.scrollz.cryptoview.data.local.CryptoViewDataBase
import com.scrollz.cryptoview.data.remote.CoinApi
import com.scrollz.cryptoview.data.remote.CoinPaprikaApi
import com.scrollz.cryptoview.domain.model.FavoriteCoin
import com.scrollz.cryptoview.domain.repository.CryptoViewRepository
import com.scrollz.cryptoview.utils.createDetailedCoin
import com.scrollz.cryptoview.utils.networkBoundResource
import com.scrollz.cryptoview.utils.toCoin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CryptoViewRepositoryImpl @Inject constructor(
    private val coinPaprikaApi: CoinPaprikaApi,
    private val coinApi: CoinApi,
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
                    iconUrl = icons.find { it.assetId.uppercase() == coinDto.symbol.uppercase() }?.url
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
}
