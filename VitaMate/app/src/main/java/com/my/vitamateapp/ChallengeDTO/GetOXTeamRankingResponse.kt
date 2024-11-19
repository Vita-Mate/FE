package com.my.vitamateapp.ChallengeDTO

data class GetOXTeamRankingResponse(
    val isSuccess : Boolean,
    val code : String,
    val message : String,
    val result : List<TeamRank>,
    val success: Boolean
)

data class TeamRank(
    val rank : Int,
    val nickname : String,
    val totalExerciseTime : String
)