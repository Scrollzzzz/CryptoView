package com.scrollz.cryptoview.domain.repository

import com.scrollz.cryptoview.domain.model.Coin
import com.scrollz.cryptoview.domain.model.DeferredNotification
import com.scrollz.cryptoview.domain.model.DetailedCoin
import com.scrollz.cryptoview.domain.model.HistoricalTicks
import com.scrollz.cryptoview.domain.model.Notification
import com.scrollz.cryptoview.utils.Resource
import kotlinx.coroutines.flow.Flow

interface CryptoViewRepository {

    fun getCoins(): Flow<Resource<List<Coin>>>

    suspend fun updateCoinIcons(coins: List<Coin>)

    suspend fun getCoinIconsURLs(): List<String?>

    fun getDetailedCoin(id: String): Flow<Resource<DetailedCoin?>>

    fun getHistoricalTicks(id: String): Flow<Resource<HistoricalTicks>>

    suspend fun getCoin(id: String): Coin

    fun getFavorites(): Flow<List<String>>

    fun isCoinFavorite(id: String): Flow<Boolean>

    suspend fun toggleFavorite(id: String)

    fun getNotification(coinID: String): Flow<Notification?>

    suspend fun addNotification(notification: Notification)

    suspend fun deleteNotification(coinID: String)

    suspend fun getDeferredNotificationsDeleting(): List<String>

    suspend fun addDeferredNotificationEmptinessChecking(
        deferredNotification: DeferredNotification
    ): Boolean

    suspend fun getCurrentTime(): String

}
