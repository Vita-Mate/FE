package com.my.vitamateapp.ChallengeDTO

import com.my.vitamateapp.Challenge.OXTeamRank

data class GetOXTeamRankingResponse(
    val isSuccess : Boolean,
    val code : String,
    val message : String,
    val result : List<OXTeamRank>,
    val success: Boolean
)

data class OXTeamRank(
    val rank : Int,
    val nickname : String,
    val totalExerciseTime : String
)