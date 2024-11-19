package com.my.vitamateapp.Api

import com.my.vitamateapp.ChallengeDTO.GetExerciseMyRecordResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface GetExerciseMyRecordApi {
    @GET("/challenges/exercise/{category}/myRecord")
    fun getExerciseMyRecord(
        @Header("Authorization") Token: String,  // Access Token 추가
        @Path("category") category: String,      // Path 변수 추가
    ): Call<GetExerciseMyRecordResponse>
}