package com.my.vitamateapp.ChallengeDTO

data class CreateChallengeResponse(
    val isSuccess : Boolean,
    val code : String,
    val message : String,
    val result : Result,
    val success : Boolean
)
data class Result (
    val challengeId : Long?,
    val title : String,
    val status : Status,
    val createdAt : String
)
enum class Status (val value : String) {
    WAITING ("WAITING"),
    IN_PROGRESS ("IN_PROGRESS"),
    FINISHED ("FINISHED"),
    CANCELLED ("CANCELLED")
}
