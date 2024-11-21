package com.my.vitamateapp.ChallengeDTO

data class GetOXTeamRecordResponse(
    val isSuccess : Boolean,
    val code : String,
    val message : String,
    val result : List<OXTeamRecord>,
    val success: Boolean
)

data class OXTeamRecord(
    val rank : Int,
    val nickname : String,
    val totalExerciseTime : String
)