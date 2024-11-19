package com.my.vitamateapp.Api

import com.my.vitamateapp.ChallengeDTO.GetOXMyRecordResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface GetOXMyRecordApi {
    @GET("/challenges/OX/{category}/myRecord")
    fun getOXmyRecord(
        @Header("Authorization") Token: String,  // Access Token 추가
        @Path("category") category: String,      // Path 변수 추가
    ): Call<GetOXMyRecordResponse>
}