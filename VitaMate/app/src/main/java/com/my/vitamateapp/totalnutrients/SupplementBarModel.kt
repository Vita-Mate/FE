package com.my.vitamateapp.totalnutrients
data class SupplementBarModel(
    val name: String,
    val currentAmount: Float,
    val targetAmount: Float,
    val color: Int // 그래프 색상
)
