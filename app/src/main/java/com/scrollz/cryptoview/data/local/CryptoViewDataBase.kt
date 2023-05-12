package com.scrollz.cryptoview.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.scrollz.cryptoview.domain.model.Coin
import com.scrollz.cryptoview.domain.model.DetailedCoin
import com.scrollz.cryptoview.domain.model.FavoriteCoin

@Database(entities = [Coin::class, DetailedCoin::class, FavoriteCoin::class], version = 4)

abstract class CryptoViewDataBase : RoomDatabase() {
    abstract fun cryptoViewDao(): CryptoViewDao

    companion object {
        const val DATABASE_NAME = "crypto_view"
    }
}
