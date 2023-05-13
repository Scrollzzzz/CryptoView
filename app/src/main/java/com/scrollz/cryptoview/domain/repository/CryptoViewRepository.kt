package com.scrollz.cryptoview.domain.repository

import com.scrollz.cryptoview.domain.model.Coin
import com.scrollz.cryptoview.domain.model.DetailedCoin
import com.scrollz.cryptoview.domain.model.HistoricalTicks
import com.scrollz.cryptoview.utils.Resource
import kotlinx.coroutines.flow.Flow

interface CryptoViewRepository {

    fun getCoins(): Flow<Resource<List<Coin>>>

    fun getDetailedCoin(id: String): Flow<Resource<DetailedCoin?>>

    fun getHistoricalTicks(id: String): Flow<Resource<HistoricalTicks>>

    fun getFavorites(): Flow<List<String>>

    fun isCoinFavorite(id: String): Flow<Boolean>

    suspend fun toggleFavorite(id: String)
}
