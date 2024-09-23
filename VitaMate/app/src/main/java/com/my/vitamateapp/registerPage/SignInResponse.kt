package com.my.vitamateapp.registerPage

data class SignInResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Result,
    val success: Boolean
) {
    data class Result(
        val grantType: String,
        val accessToken: String,
        val refreshToken: String
    )
}