package com.my.vitamateapp.ChallengeDTO

data class GetOXMyRecordResponse(
    val isSuccess : Boolean,
    val code : String,
    val message : String,
    val result : List<OXMyRecord>,
    val success: Boolean
)

data class OXMyRecord(
    val rank : Int,
    val nickname : String,
    val totalExerciseTime : String
)