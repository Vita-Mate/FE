package com.my.vitamateapp.Api

import com.my.vitamateapp.mySupplement.ScrapResponse
import com.my.vitamateapp.mySupplement.SupplementsScrapResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface SupplementsScrapApi {
    // 스크랩 추가 API
    @POST("supplements/{supplementId}/scrap")
    suspend fun scrapSupplements(
        @Header("Authorization") accessToken: String,
        @Path("supplementId") supplementId: Int
    ): Response<SupplementsScrapResponse>

    // 스크랩 삭제 API
    @DELETE("supplements/{supplementId}/scrap")
    suspend fun deleteScrapSupplements(
        @Header("Authorization") accessToken: String,
        @Path("supplementId") supplementId: Int
    ): Response<SupplementsScrapResponse>

    // 스크랩 목록 조회 API
    @GET("supplements/scrap")
    suspend fun getScrapSupplements(
        @Header("Authorization") accessToken: String,
    ): ScrapResponse
}
