package com.my.vitamateapp.ChallengeDTO

data class AddExerciseResponseRequest(
    val exerciseType: String,
    val Intensity: Intensity,
    val startHour: Int,
    val startMinute: Int,
    val endHour: Int,
    val endMinute: Int,
    val comment: String
)

enum class Intensity(val value: String) {
    VERY_EASY("VERY_EASY"),
    EASY("EASY"),
    MODERATE("MODERATE"),
    VERY_HARD("VERY_HARD"),
    EXTREME("EXTREME")
}
