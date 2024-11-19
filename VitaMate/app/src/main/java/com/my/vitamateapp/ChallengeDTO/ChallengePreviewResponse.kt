package com.my.vitamateapp.ChallengeDTO

data class ChallengePreviewResponse(
    val isSuccess : Boolean,
    val code : String,
    val message : String,
    val result : Participating,
    val success : Boolean
)

data class Participating(
    val title : String,
    val startDate : String,
    val endDate : String,
    val maxParticipants : Int,
    val currentParticipants : Int,
    val weeklyFrequency : Int,
    val challengeId : Long,
    val dday : Int
)