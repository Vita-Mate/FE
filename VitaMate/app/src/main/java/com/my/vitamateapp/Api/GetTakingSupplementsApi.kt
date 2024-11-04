package com.my.vitamateapp.Api

import com.my.vitamateapp.mySupplement.GetTakingSupplementsResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GetTakingSupplementsApi {
    // 복용 중인 영양제 조회 API
    @GET("supplements/taking")
    suspend fun getTakingSupplements(
        @Header("Authorization") accessToken: String, // Authorization 헤더 추가
        @Query("page") page: Int = 0 // 페이지 번호, 기본값은 0
    ): GetTakingSupplementsResponse // 응답 모델 변경
}
