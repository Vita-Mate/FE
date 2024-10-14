package com.my.vitamateapp.Challenge

data class CreateChallengeRequest(
    val category : Category,
    val title : String,
    val description : String,
    val startDate : String,
    val duration : Duration,
    val maxParticipant : Int,
    val minParticipant : Int,
    val weeklyFrequency : Int
){
    fun isValid(): Boolean {
        return minParticipant in 1..10 && maxParticipant in 2..10 && minParticipant <= maxParticipant
    }
}

    enum class Category(val value: String) {
        EXERCISE("EXERCISE"),
        QUIT_ALCOHOL("QUIT_ALCOHOL"),
        QUIT_SMOKE("QUIT_SMOKE")
    }

    enum class Duration(val value: String) {
        ONE_WEEK("ONE_WEEK"),
        ONE_MONTH("ONE_MONTH"),
        THREE_MONTHS("THREE_MONTHS"),
        SIX_MONTHS("SIX_MONTHS"),
        ONE_YEAR("ONE_YEAR")
    }
