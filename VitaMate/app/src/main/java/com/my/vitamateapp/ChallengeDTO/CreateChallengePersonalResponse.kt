package com.my.vitamateapp.ChallengeDTO

data class CreateChallengePersonalResponse(
    val isSuccss : Boolean,
    val code : String,
    val message : String,
    val result : IndResult,
    val success : Boolean
)
data class IndResult (
    val challengeId : Long?,
    val title : String,
    val status : IndStatus,
    val createdAt : String
)
enum class IndStatus (val value : String) {
    WAITING ("WAITING"),
    IN_PROGRESS ("IN_PROGRESS"),
    FINISHED ("FINISHED"),
    CANCELLED ("CANCELLED")
}
