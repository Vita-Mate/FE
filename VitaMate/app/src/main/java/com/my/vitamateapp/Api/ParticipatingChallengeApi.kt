package com.my.vitamateapp.Api

import com.my.vitamateapp.Challenge.ChallengePreviewResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ParticipatingChallengeApi {
    @GET("/challenges/{category}/my") // 기본 엔드포인트
    fun ParticipatingList(
        @Header("Authorization") token: String,  // Access Token 추가
        @Path("category") category: String      // 쿼리 변수로 수정
    ): Call<ChallengePreviewResponse>
}
