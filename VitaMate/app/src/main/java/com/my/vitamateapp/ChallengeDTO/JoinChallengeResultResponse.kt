package com.my.vitamateapp.ChallengeDTO

data class JoinChallengeResultResponse(
    val isSuccess : Boolean,
    val code : String,
    val message : String,
    val result : JoinResult,
    val success : Boolean
)

data class JoinResult(
    val joinedAt : String,
    val challengeId : Long
)
