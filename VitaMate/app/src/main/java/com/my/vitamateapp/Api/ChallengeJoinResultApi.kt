package com.my.vitamateapp.network

import com.my.vitamateapp.ChallengeDTO.ChallengePreviewResponse
import com.my.vitamateapp.ChallengeDTO.JoinChallengeResultResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ChallengeJoinResultApi {


    // 챌린지 참여 메서드 정의
    @POST("/challenges/{challengeId}")
    fun joinChallenge(
        @Header("Authorization") Token: String,
        @Path("challengeId") challengeId: Long
    ): Call<JoinChallengeResultResponse>

    // 챌린지 정보 조회 메서드 정의 (GET)
    @GET("/challenges/{challengeId}")
    fun getChallengeDetails(
        @Header("Authorization") Token: String,
        @Path("challengeId") challengeId: Long
    ): Call<ChallengePreviewResponse>
}