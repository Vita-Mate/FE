package com.my.vitamateapp.network

import com.my.vitamateapp.Challenge.JoinChallengeResultResponse
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface JoinResultApi {

    // 챌린지 참여 메서드 정의
    @POST("challenges/{challengeId}")
    fun joinChallenge(
        @Path("challengeId") challengeId: String,
        @Header("Authorization") Token: String
    ): Call<JoinChallengeResultResponse>
}