package com.my.vitamateapp.Api

import com.my.vitamateapp.ChallengeDTO.GetOXTeamRankingResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface GetOXTeamRankingApi {
    @GET("/challenges/OX/{category}/Ranking")
    fun getOXRanking(
        @Header("Authorization") Token: String,  // Access Token 추가
        @Path("category") category: String,      // Path 변수 추가
    ): Call<GetOXTeamRankingResponse>
}