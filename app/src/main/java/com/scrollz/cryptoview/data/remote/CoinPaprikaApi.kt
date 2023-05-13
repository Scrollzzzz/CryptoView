package com.scrollz.cryptoview.data.remote

import com.scrollz.cryptoview.domain.model.dto.CoinDTO
import com.scrollz.cryptoview.domain.model.dto.CoinInfoDTO
import com.scrollz.cryptoview.domain.model.dto.TickDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

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

    @GET("tickers/{coin_id}/historical")
    suspend fun getHistoricalTicks(
        @Path("coin_id") id: String,
        @Query("start") startDateTime: String,
        @Query("interval") interval: String
    ): List<TickDTO>
}
