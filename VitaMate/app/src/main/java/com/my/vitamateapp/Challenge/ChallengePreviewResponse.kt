package com.my.vitamateapp.Challenge

data class ChallengePreviewResponse(
    val isSuccess : Boolean,
    val code : String,
    val message : String,
    val result : Participating,
    val success : Boolean
)

data class Participating(
    val title : String,
    val maxParticipants : Int,
    val currentParticipants : Int,
    val weeklyFrequency : Int,
    val challengeId : Long,
    val dday : Int
)