package com.scrollz.cryptoview.domain.model.dto

import com.google.gson.annotations.SerializedName

data class IconDTO(
    @SerializedName("asset_id")
    val assetId: String,
    @SerializedName("url")
    val url: String
)
