package com.my.vitamateapp.Api

import com.my.vitamateapp.Challenge.ChallengeListResponse
import com.my.vitamateapp.Challenge.CreateChallengeRequest
import com.my.vitamateapp.Challenge.CreateChallengeResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ChallengeListApi {
    @GET("/challenges/{category}")
    fun challengeList(
        @Header("Authorization") Token: String,  // Access Token 추가
        @Path("category") category: String,      // Path 변수 추가
    ): Call<ChallengeListResponse>
}
