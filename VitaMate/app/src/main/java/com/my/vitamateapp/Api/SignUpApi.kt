package com.my.vitamateapp.Api

import com.my.vitamateapp.registerPage.SignUpRequest
import com.my.vitamateapp.registerPage.SignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpApi {
    @POST("members/sign-up")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): Response<SignUpResponse>
}