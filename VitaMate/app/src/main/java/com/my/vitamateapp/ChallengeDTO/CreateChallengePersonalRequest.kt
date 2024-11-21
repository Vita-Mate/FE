package com.my.vitamateapp.ChallengeDTO

data class CreateChallengePersonalRequest(
    val category : IndCategory,
    val startDate : String,
    val duration : IndDuration,
    val weeklyFrequency : Int
)

enum class IndCategory(val value: String) {
    EXERCISE("EXERCISE"),
    QUIT_ALCOHOL("QUIT_ALCOHOL"),
    QUIT_SMOKE("QUIT_SMOKE")
}

enum class IndDuration(val value: String) {
    ONE_WEEK("ONE_WEEK"),
    ONE_MONTH("ONE_MONTH"),
    THREE_MONTHS("THREE_MONTHS"),
    SIX_MONTHS("SIX_MONTHS"),
    ONE_YEAR("ONE_YEAR")
}
