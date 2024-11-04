package com.my.vitamateapp.mySupplement

// 리뷰 작성 요청 모델
data class WriteReviewRequest(
    val grade: Int,       // 별점 (1 ~ 5 사이 값)
    val content: String   // 리뷰 내용
)

// 리뷰 작성 응답 모델
data class WriteReviewResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: WriteReviewResult,
    val success: Boolean
)

// 작성된 리뷰 결과 모델
data class WriteReviewResult(
    val reviewId: Int,
    val nickname: String,
    val content: String,
    val grade: Int,
    val createdDate: String // ISO-8601 형식 (예: "2024-11-03T03:34:22.958Z")
)

// 리뷰 조회 응답 모델
data class GetReviewResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: ReviewListResult,
    val success: Boolean
)

// 리뷰 리스트 결과 모델
data class ReviewListResult(
    val reviewList: List<ReviewItem>, // 각 리뷰 항목의 리스트
    val listSize: Int,
    val totalPage: Int,
    val totalElements: Int,
    val isFirst: Boolean,
    val isLast: Boolean
)

// 개별 리뷰 항목 모델
data class ReviewItem(
    val reviewId: Int,
    val nickname: String,
    val content: String,
    val grade: Int,
    val createdDate: String // ISO-8601 형식
)