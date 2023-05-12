package com.scrollz.cryptoview.domain.model

import androidx.room.PrimaryKey

data class CoinView(
    @PrimaryKey val id: String,
    val name: String,
    val symbol: String,
    val rank: Int,
    val price: String,
    val percentChange24h: String,
    val iconUrl: String? = null,
    val isPercentPositive: Boolean
)
