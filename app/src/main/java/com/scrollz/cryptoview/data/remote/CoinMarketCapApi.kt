package com.scrollz.cryptoview.data.remote

import com.scrollz.cryptoview.domain.model.dto.CMCResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CoinMarketCapApi {

    @Headers("X-CMC_PRO_API_KEY: 2115bc9f-1cfe-41f4-adca-e687e88f2191")
    @GET("v2/cryptocurrency/info")
    suspend fun getIcons(
        @Query("symbol") ids: String,
        @Query("aux") aux: String = "logo",
        @Query("skip_invalid") skipInvalid: Boolean = true
    ): CMCResponse

}