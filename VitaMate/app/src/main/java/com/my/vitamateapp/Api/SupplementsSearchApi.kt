package com.my.vitamateapp.Api

import com.my.vitamateapp.mySupplement.SupplementsSearchResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SupplementsSearchApi {
    // 영양제 검색 API
    @GET("supplements/search")
    suspend fun searchSupplements(
        @Header("Authorization") accessToken: String,  // Authorization 헤더 추가
        @Query("searchType") searchType: String,
        @Query("keyword") keyword: String,
        @Query("page") page: Int = 0,
        @Query("pageSize") pageSize: Int = 15
    ): SupplementsSearchResponse
}

