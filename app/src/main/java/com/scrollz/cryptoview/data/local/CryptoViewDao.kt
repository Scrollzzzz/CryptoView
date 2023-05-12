package com.scrollz.cryptoview.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.scrollz.cryptoview.domain.model.Coin
import com.scrollz.cryptoview.domain.model.DetailedCoin
import com.scrollz.cryptoview.domain.model.FavoriteCoin
import kotlinx.coroutines.flow.Flow

@Dao
interface CryptoViewDao {

    @Query("SELECT * FROM coin")
    fun getCoinsList(): Flow<List<Coin>>

    @Query("SELECT * FROM detailed_coin WHERE id = :id")
    fun getDetailedCoin(id: String): Flow<DetailedCoin?>

    @Query("SELECT id FROM favorite_coin")
    fun getFavorites(): Flow<List<String>>

    @Query("SELECT CASE WHEN EXISTS(SELECT 1 FROM favorite_coin WHERE id = :id) THEN 1 ELSE 0 END AS is_favorite")
    fun isCoinFavorite(id: String): Flow<Boolean>

    @Insert(entity = Coin::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoins(coins: List<Coin>)

    @Insert(entity = FavoriteCoin::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIntoFavorites(favoriteCoin: FavoriteCoin)

    @Upsert(entity = DetailedCoin::class)
    suspend fun upsertDetailedCoin(detailedCoin: DetailedCoin)

    @Query("DELETE FROM coin")
    suspend fun deleteAllCoins()

    @Query("DELETE FROM favorite_coin WHERE id = :id")
    suspend fun deleteFromFavorites(id: String)

}
