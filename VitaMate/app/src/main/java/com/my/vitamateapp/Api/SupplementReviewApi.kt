package com.my.vitamateapp.Api

import com.my.vitamateapp.mySupplement.GetReviewResponse
import com.my.vitamateapp.mySupplement.WriteReviewResponse
import com.my.vitamateapp.mySupplement.WriteReviewRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SupplementReviewApi {
    // 리뷰 작성 API
    @POST("supplements/{supplementId}/review")
    suspend fun writeSupplementsReview(
        @Header("Authorization") accessToken: String,
        @Path("supplementId") supplementId: Int,
        @Body requestBody: WriteReviewRequest
    ): WriteReviewResponse

    // 리뷰 조회 API
    @GET("supplements/{supplementId}/review")
    suspend fun getSupplementReviews(
        @Header("Authorization") accessToken: String,
        @Path("supplementId") supplementId: Int,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): GetReviewResponse
}
