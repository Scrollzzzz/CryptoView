package com.scrollz.cryptoview.domain.model.dto

import com.google.gson.annotations.SerializedName
import com.scrollz.cryptoview.domain.model.Icon

data class CMCResponse(
    @SerializedName("data")
    val data: Map<String, List<Icon>>,
    @SerializedName("status")
    val status: Status
)
