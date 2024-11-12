package com.my.vitamateapp.network

import com.my.vitamateapp.Api.ParticipatingChallengeApi
import com.my.vitamateapp.Challenge.ChallengePreviewResponse
import com.my.vitamateapp.Challenge.JoinChallengeResultResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ChallengeJoinResultApi {


    // 챌린지 참여 메서드 정의
    @POST("challenges/{challengeId}")
    fun joinChallenge(
        @Path("challengeId") challengeId: String,
        @Header("Authorization") Token: String
    ): Call<JoinChallengeResultResponse>

    // 챌린지 정보 조회 메서드 정의 (GET)
    @GET("challenges/{challengeId}")
    fun getChallengeDetails(
        @Path("challengeId") challengeId: String,
        @Header("Authorization") Token: String
    ): Call<ChallengePreviewResponse>
}