package com.my.vitamateapp.Api

import com.my.vitamateapp.Challenge.AddExerciseRecordResponse
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface AddExerciseRecordApi {

    // 챌린지 참여 메서드 정의
    @POST("challenges/{challengeId}/record")
    fun addExerciseRecord(
        @Path("challengeId") challengeId: String,
        @Header("Authorization") Token: String
    ): Call<AddExerciseRecordResponse>
}