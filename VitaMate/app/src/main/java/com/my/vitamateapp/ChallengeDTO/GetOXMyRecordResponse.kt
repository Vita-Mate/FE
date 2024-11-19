package com.my.vitamateapp.ChallengeDTO

data class GetOXMyRecordResponse(
    val isSuccess : Boolean,
    val code : String,
    val message : String,
    val result : MyRecord,
    val success: Boolean
)

data class MyRecord(
    val rank : Int,
    val nickname : String,
    val totalExerciseTime : String
)