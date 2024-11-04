package com.my.vitamateapp.mySupplement

data class AddedSupplementModel(
    val id: Int,//영양제ID
    val name: String,
    val duration: Int // 복용 기간 추가
)