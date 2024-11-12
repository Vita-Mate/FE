package com.my.vitamateapp.registerPage

data class NicknameCheckResponse(
    val isSuccss: Boolean,
    val code: String,
    val message: String,
    val result: String,
    val success: Boolean
)