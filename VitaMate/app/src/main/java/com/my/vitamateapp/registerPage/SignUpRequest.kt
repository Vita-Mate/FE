package com.my.vitamateapp.registerPage

data class SignUpRequest(
    var email: String,
    var nickname: String,
    var birthDay: String,
    var height: Int,
    var weight: Int,
    var gender: Int, // 1은 남자, 2는 여자
    val bmr: Int? = null // 널이면 서버가 키, 몸무게로 계산
)
