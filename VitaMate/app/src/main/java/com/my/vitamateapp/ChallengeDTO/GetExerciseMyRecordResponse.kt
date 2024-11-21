package com.my.vitamateapp.ChallengeDTO

data class GetExerciseMyRecordResponse(
    val isSuccess : Boolean,
    val code : String,
    val message : String,
    val result : List<GetMyResult>,
    val success : Boolean
)

data class GetMyResult(
    val nickname : String,
    val imageURL : String,
    val exerciseType : String,
    val startTime : String,
    val endTime : String,
    val comment : String,
    val exerciseRecordId : Long
)
