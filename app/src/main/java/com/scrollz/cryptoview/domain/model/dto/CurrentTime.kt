package com.scrollz.cryptoview.domain.model.dto

import com.google.gson.annotations.SerializedName

data class CurrentTime(
    @SerializedName("dateTime")
    val dateTime: String
)
