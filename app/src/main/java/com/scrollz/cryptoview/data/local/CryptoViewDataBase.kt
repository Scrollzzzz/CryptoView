package com.scrollz.cryptoview.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.scrollz.cryptoview.domain.model.Coin
import com.scrollz.cryptoview.domain.model.DetailedCoin
import com.scrollz.cryptoview.domain.model.FavoriteCoin
import com.scrollz.cryptoview.domain.model.Tick

@Database(
    entities = [Coin::class, DetailedCoin::class, FavoriteCoin::class, Tick::class],
    version = 5
)

abstract class CryptoViewDataBase : RoomDatabase() {
    abstract fun cryptoViewDao(): CryptoViewDao

    companion object {
        const val DATABASE_NAME = "crypto_view"
    }
}
