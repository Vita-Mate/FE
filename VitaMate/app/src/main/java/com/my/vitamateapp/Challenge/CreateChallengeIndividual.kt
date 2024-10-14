package com.my.vitamateapp.Challenge

import java.io.Serializable

data class CreateChallengeIndividual(
    val category: String,       // 카테고리
    val startDate: String,        // 시작 날짜: YYYY-MM-DD 형식의 String
    val weeklyFrequency: Int,     // 주간 빈도
    val duration: String,       // 기간
    val description: String,      // 설명
) : Serializable
