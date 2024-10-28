package com.my.vitamateapp.mySupplement

//요청 데이터 클래스
data class TakingSupplementModel(
    var startDate: String
)

// 응답 데이터 클래스
data class TakingSupplementResponse(
    val isSuccss: Boolean,
    val code: String,
    val message: String,
    val result: Result,
    val success: Boolean
) {data class Result(
    val supplementID: Int,
    val supplementName: String,
    val supplementBrand: String,
    val duration: Int
    )
}
