package com.my.vitamateapp.registerPage

data class SignUpResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Result,
    val success: Boolean
)

data class Result(
    val id: Int,
    val email: String,
    val nickname: String,
    val age: Int,
    val createdAt: String
)
