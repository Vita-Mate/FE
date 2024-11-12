package com.my.vitamateapp.Api

import com.my.vitamateapp.registerPage.NicknameCheckResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CheckNicknameApi {

    // 닉네임 중복 확인 API
    @GET("members/check-nickname")
    suspend fun checkNickname(
        @Query("nickname") nickname: String
    ): Response<NicknameCheckResponse>
}