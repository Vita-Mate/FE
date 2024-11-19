package com.my.vitamateapp.ChallengeDTO

data class GetExerciseTeamRecordResponse(
    val isSuccess : Boolean,
    val code : String,
    val message : String,
    val result : GetResult,
    val success : Boolean
)

data class GetResult(
    val nickname : String,
    val imageURL : String,
    val exerciseType : String,
    val startTime : String,
    val endTime : String,
    val comment : String,
    val exerciseRecordId : Long
)
