package com.my.vitamateapp.ChallengeDTO

data class GetOXTeamRecordResponse(
    val isSuccess : Boolean,
    val code : String,
    val message : String,
    val result : List<TeamRecord>,
    val success: Boolean
)

data class TeamRecord(
    val rank : Int,
    val nickname : String,
    val totalExerciseTime : String
)