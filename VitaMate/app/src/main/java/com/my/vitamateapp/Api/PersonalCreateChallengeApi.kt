package com.my.vitamateapp.Api

import com.my.vitamateapp.ChallengeDTO.CreateChallengePersonalRequest
import com.my.vitamateapp.ChallengeDTO.CreateChallengePersonalResponse

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface PersonalCreateChallengeApi {
    @POST("/challenges/personal")
    fun createPersonalChallenge(
        @Header("Authorization") Token: String,  // Access Token 추가
        @Body request: CreateChallengePersonalRequest
    ): Call<CreateChallengePersonalResponse>
}
