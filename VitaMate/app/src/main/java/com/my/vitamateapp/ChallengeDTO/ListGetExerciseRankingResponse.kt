package com.my.vitamateapp.ChallengeDTO

data class ListGetExerciseRankingResponse(
    val isSuccess : Boolean,
    val code : String,
    val message : String,
    val result : List<TeamRankItem>,
    val success: Boolean
)

data class TeamRankItem(
    val rank : Int,
    val nickname : String,
    val totalExerciseTime : String
)