package com.my.vitamateapp.ChallengeDTO

data class GetOXTeamRankingResponse(
    val isSuccess : Boolean,
    val code : String,
    val message : String,
    val result : List<OXTeamRank>,
    val success: Boolean
)

data class OXTeamRank(
    var rank : Int,
    val nickname : String,
    val successCount : Int
)