package com.my.vitamateapp.ChallengeDTO


data class ChallengeListResponse (
    val isSuccess : Boolean,
    val code : String,
    val message : String,
    val result : ChallengeList,
    val success : Boolean
)

data class ChallengeList (
    val challengeList : List<Challenge>?,
    val lastSize : Int,
    val totalPage : Int,
    val totalElements : Long,
    val isFirst : Boolean,
    val isLast : Boolean
)

data class  Challenge (
    val title : String,
    val startDate : String,
    val endDate : String,
    val maxParticipants : Int,
    val currentParticipants : Int,
    val weeklyFrequency : Int,
    val challengeId : Long,
    val dday : Int
)

