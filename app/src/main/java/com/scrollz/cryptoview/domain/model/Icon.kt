package com.scrollz.cryptoview.domain.model

import com.google.gson.annotations.SerializedName

data class Icon(
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("logo")
    val iconUrl: String?
)
