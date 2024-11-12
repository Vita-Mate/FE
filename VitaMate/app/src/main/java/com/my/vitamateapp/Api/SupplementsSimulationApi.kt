package com.my.vitamateapp.Api

import com.my.vitamateapp.mySupplement.SimulationResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface SupplementsSimulationApi {
    // 섭취중인 영양제 + 스크랩한 영양제 영양소 합산 API
    @GET("supplements/simulation")
    suspend fun simulatioSupplements(
        @Header("Authorization") accessToken: String,
    ): SimulationResponse
}