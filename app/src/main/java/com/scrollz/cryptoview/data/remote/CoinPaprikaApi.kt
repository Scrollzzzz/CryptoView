package com.scrollz.cryptoview.data.remote

import com.scrollz.cryptoview.domain.model.dto.CoinDTO
import com.scrollz.cryptoview.domain.model.dto.CoinInfoDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface CoinPaprikaApi {

    @GET("tickers")
    suspend fun getCoins(): List<CoinDTO>

    @GET("tickers/{coin_id}")
    suspend fun getCoin(
        @Path("coin_id") id: String
    ): CoinDTO

    @GET("coins/{coin_id}")
    suspend fun getCoinInfo(
        @Path("coin_id") id: String
    ): CoinInfoDTO
}
