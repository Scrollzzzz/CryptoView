package com.scrollz.cryptoview.data.remote

import com.scrollz.cryptoview.domain.model.dto.CurrentTime
import retrofit2.http.GET
import retrofit2.http.Query

interface TimeApi {

    @GET("Time/current/zone")
    suspend fun getCurrentTime(
        @Query("timeZone") timeZone: String = "UTC"
    ): CurrentTime

}
