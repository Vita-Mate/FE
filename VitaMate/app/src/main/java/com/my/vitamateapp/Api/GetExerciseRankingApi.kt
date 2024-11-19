package com.my.vitamateapp.Api

import com.my.vitamateapp.ChallengeDTO.ListGetExerciseRankingResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface GetExerciseRankingApi {
    @GET("/challenges/exercise/{category}/ranking")
    fun getExerciseRanking(
        @Header("Authorization") Token: String,  // Access Token 추가
        @Path("category") category: String,      // Path 변수 추가
    ): Call<ListGetExerciseRankingResponse>
}