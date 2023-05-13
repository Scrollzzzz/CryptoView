package com.scrollz.cryptoview.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "historical_tick")

data class Tick(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val coinID: String,
    val period: String,
    val timestamp: String,
    val price: Double,
    val volume24h: Long,
    val marketCap: Long
)
