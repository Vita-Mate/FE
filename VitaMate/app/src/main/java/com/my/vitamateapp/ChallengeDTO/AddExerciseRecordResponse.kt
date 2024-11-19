package com.my.vitamateapp.ChallengeDTO

data class AddExerciseRecordResponse(
    val isSuccess : Boolean,
    val code : String,
    val message : String,
    val result : RecordResult,
    val success : Boolean
)

data class RecordResult(
    val recordId : Long
)

