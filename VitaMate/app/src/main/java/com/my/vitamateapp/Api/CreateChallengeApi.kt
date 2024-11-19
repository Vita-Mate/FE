package com.my.vitamateapp.Api

import com.my.vitamateapp.ChallengeDTO.CreateChallengeRequest
import com.my.vitamateapp.ChallengeDTO.CreateChallengeResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface CreateChallengeApi {
    @POST("/challenges/")
    fun createChallenge(
        @Header("Authorization") Token: String,  // Access Token 추가
        @Body request: CreateChallengeRequest
    ): Call<CreateChallengeResponse>
}
