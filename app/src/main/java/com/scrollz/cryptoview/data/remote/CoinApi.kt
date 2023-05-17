package com.scrollz.cryptoview.data.remote

import com.scrollz.cryptoview.domain.model.dto.IconDTO
import retrofit2.http.GET
import retrofit2.http.Headers

interface CoinApi {

    @Headers("X-CoinAPI-Key: B7C3D193-4943-4C7E-A3CB-DB3FA19D34E7")
    @GET("assets/icons/24")
    suspend fun getIcons(): List<IconDTO>

}
