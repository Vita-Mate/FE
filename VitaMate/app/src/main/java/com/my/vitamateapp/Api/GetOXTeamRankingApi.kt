package com.my.vitamateapp.Api

import com.my.vitamateapp.ChallengeDTO.GetOXTeamRankingResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface GetOXTeamRankingApi {
    @GET("/challenges/OX/{challengeId}/Ranking")
    fun getOXRanking(
        @Header("Authorization") Token: String,  // Access Token 추가
        @Path("challengeId") challengeId: Long,      // Path 변수 추가
        @Query("date") selectedDate: String              // 쿼리 파라미터로 날짜 추가
    ): Call<GetOXTeamRankingResponse>
}