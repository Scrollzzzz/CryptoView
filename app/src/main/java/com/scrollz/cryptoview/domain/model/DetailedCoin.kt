package com.scrollz.cryptoview.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "detailed_coin")

data class DetailedCoin(
    @PrimaryKey val id: String,
    val name: String,
    val symbol: String,
    val rank: Int,
    val iconUrl: String,
    val type: String,
    val price: Double,
    val percentChange1h: Double,
    val percentChange12h: Double,
    val percentChange24h: Double,
    val percentChange7d: Double,
    val percentChange30d: Double,
    val percentChange1y: Double,
    val circulatingSupply: Double,
    val totalSupply: Double,
    val maxSupply: Double,
    val volume24h: Double,
    val volumeChange24h: Double,
    val marketCap: Double,
    val marketCapChange24h: Double,
    val priceATH: Double,
    val percentFromATHPrice: Double,
    val last_updated: String
)
