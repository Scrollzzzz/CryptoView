package com.scrollz.cryptoview.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coin")

data class Coin(
    @PrimaryKey val id: String,
    val name: String,
    val symbol: String,
    val rank: Int,
    val price: Double,
    val percentChange24h: Double,
    val iconUrl: String? = null
)
