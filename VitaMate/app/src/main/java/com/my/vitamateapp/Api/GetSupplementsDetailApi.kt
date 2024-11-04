package com.my.vitamateapp.Api

import com.my.vitamateapp.mySupplement.GetDetailResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface GetSupplementsDetailApi {
    // 영양제 상세 정보 조회 API
    @GET("supplements/{supplementId}")
    suspend fun getSupplementsDetail(
        @Header("Authorization") accessToken: String,
        @Path("supplementId") supplementId: Int
    ) : GetDetailResponse
}