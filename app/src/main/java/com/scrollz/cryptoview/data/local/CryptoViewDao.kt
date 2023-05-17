package com.scrollz.cryptoview.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.scrollz.cryptoview.domain.model.Coin
import com.scrollz.cryptoview.domain.model.DeferredNotification
import com.scrollz.cryptoview.domain.model.DetailedCoin
import com.scrollz.cryptoview.domain.model.FavoriteCoin
import com.scrollz.cryptoview.domain.model.Tick
import com.scrollz.cryptoview.domain.model.Notification
import kotlinx.coroutines.flow.Flow

@Dao
interface CryptoViewDao {

    // Coins
    @Query("SELECT * FROM coin")
    fun getCoinsList(): Flow<List<Coin>>

    @Insert(entity = Coin::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoins(coins: List<Coin>)

    @Query("DELETE FROM coin")
    suspend fun deleteAllCoins()


    // Detailed coins
    @Query("SELECT * FROM detailed_coin WHERE id = :id")
    fun getDetailedCoin(id: String): Flow<DetailedCoin?>

    @Upsert(entity = DetailedCoin::class)
    suspend fun upsertDetailedCoin(detailedCoin: DetailedCoin)


    // Favorite coins
    @Query("SELECT id FROM favorite_coin")
    fun getFavorites(): Flow<List<String>>

    @Query("SELECT CASE WHEN EXISTS(SELECT 1 FROM favorite_coin WHERE id = :id) THEN 1 ELSE 0 END AS is_favorite")
    fun isCoinFavorite(id: String): Flow<Boolean>

    @Insert(entity = FavoriteCoin::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIntoFavorites(favoriteCoin: FavoriteCoin)

    @Query("DELETE FROM favorite_coin WHERE id = :id")
    suspend fun deleteFromFavorites(id: String)


    // Historical ticks
    @Query("SELECT * FROM historical_tick WHERE coinID = :id")
    fun getHistoricalTicks(id: String): Flow<List<Tick>>

    @Insert(entity = Tick::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistoricalTicks(ticks: List<Tick>)

    @Query("DELETE FROM historical_tick WHERE coinID = :id")
    suspend fun deleteHistoricalTicks(id: String)


    // Notifications
    @Query("SELECT * FROM notification")
    suspend fun getNotifications(): List<Notification>

    @Query("SELECT CASE WHEN EXISTS(SELECT 1 FROM notification WHERE coinID = :id) THEN 1 ELSE 0 END AS notification_on")
    fun isNotificationOn(id: String): Flow<Boolean>

    @Upsert(entity = Notification::class)
    suspend fun upsertNotification(notification: Notification)

    @Query("DELETE FROM notification WHERE coinID = :coinID")
    suspend fun deleteNotification(coinID: String)


    // Deferred notifications
    @Query("SELECT coinID FROM deferred_notification")
    suspend fun getDeferredNotifications(): List<String>

    @Query("SELECT CASE WHEN COUNT(*) = 0 THEN 1 ELSE 0 END AS is_empty FROM deferred_notification")
    suspend fun noDeferredNotifications(): Boolean

    @Upsert(entity = DeferredNotification::class)
    suspend fun upsertDeferredNotification(deferredNotification: DeferredNotification)

    @Query("DELETE FROM deferred_notification")
    suspend fun deleteAllDeferredNotifications()

}
