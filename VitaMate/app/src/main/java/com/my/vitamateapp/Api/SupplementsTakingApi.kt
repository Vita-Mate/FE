package com.my.vitamateapp.Api

import com.my.vitamateapp.mySupplement.TakingSupplementModel
import com.my.vitamateapp.mySupplement.TakingSupplementResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface SupplementsTakingApi {

    @POST("supplements/{supplementId}/taking")
    suspend fun takingSupplements(
        @Header("Authorization") accessToken: String,
        @Path("supplementId") supplementId: Int,
        @Body requestBody: TakingSupplementModel // 추가
    ): Response<TakingSupplementResponse>
}

