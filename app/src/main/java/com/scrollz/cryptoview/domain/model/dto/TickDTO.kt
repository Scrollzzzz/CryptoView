package com.scrollz.cryptoview.domain.model.dto

import com.google.gson.annotations.SerializedName

data class TickDTO(
    @SerializedName("timestamp")
    val timestamp: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("volume_24h")
    val volume24h: Long,
    @SerializedName("market_cap")
    val marketCap: Long
)
