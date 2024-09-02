package com.my.vitamateapp.foodRecord

data class FoodItemModel(
    val foodName: String,  // 음식 이름
    val carbohydrates: Double,  // 탄수화물량 (단위: g)
    val protein: Double,        // 단백질량 (단위: g)
    val fat: Double,            // 지방량 (단위: g)
    val foodCalories: Int,     // 음식 칼로리 (예: 250kcal)
    val foodQuantity: Int      // 음식 수량 (예: 1개, 2개 등)
)
