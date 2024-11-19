package com.my.vitamateapp.Api

import com.my.vitamateapp.ChallengeDTO.DeleteChallenge
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.Path

interface DeleteChallengeResponseApi {
    @DELETE("/challenges/{challengeId}")
    fun deleteChallenge(
        @Header("Authorization") Token: String,
        @Path("challengeId") challengeId: Long
    ): Call<DeleteChallenge>
}