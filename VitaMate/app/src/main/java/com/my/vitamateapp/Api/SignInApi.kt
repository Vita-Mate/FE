package com.my.vitamateapp.Api

import com.my.vitamateapp.registerPage.SignInRequest
import com.my.vitamateapp.registerPage.SignInResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SignInApi {
    @POST("members/sign-in")
    fun signIn(@Body request: SignInRequest): Call<SignInResponse>
}