package com.scrollz.cryptoview.domain.model.dto

import com.google.gson.annotations.SerializedName

data class Quotes(
    @SerializedName("USD")
    val usd: USD
)
