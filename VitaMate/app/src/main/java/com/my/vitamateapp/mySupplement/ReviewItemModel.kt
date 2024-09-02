package com.my.vitamateapp.mySupplement

data class ReviewItemModel(
    val userNickname: String,  // 사용자 닉네임
    val userRating: Float,     // 별점 (1.0 ~ 5.0)
    val userReview: String     // 사용자가 작성한 리뷰 내용
)