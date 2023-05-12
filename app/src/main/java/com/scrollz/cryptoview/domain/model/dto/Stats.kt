package com.scrollz.cryptoview.domain.model.dto

import com.google.gson.annotations.SerializedName

data class Stats(
    @SerializedName("contributors")
    val contributors: Int,
    @SerializedName("stars")
    val stars: Int,
    @SerializedName("subscribers")
    val subscribers: Int
)
