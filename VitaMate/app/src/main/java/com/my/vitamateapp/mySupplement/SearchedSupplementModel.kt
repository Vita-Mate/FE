package com.my.vitamateapp.mySupplement

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// 요청을 위한 모델
@Parcelize
data class SupplementsSearchRequest(
    val searchType: String,
    val name: String,
    val page: Int,
    val pageSize: Int
) : Parcelable

// 검색 결과 모델
@Parcelize
data class SearchedSupplementModel(
    val supplementId: Int,
    val brand: String,
    val name: String,
    val imageURL: String?
) : Parcelable

// API 응답 모델
@Parcelize
data class SupplementsSearchResponse(
    val isSuccss: Boolean,
    val code: String,
    val message: String,
    val result: Result
) : Parcelable

// 결과 모델
@Parcelize
data class Result(
    val supplementList: List<SearchedSupplementModel>,
    val listSize: Int,
    val totalPage: Int,
    val totalElements: Int,
    val isFirst: Boolean,
    val isLast: Boolean
) : Parcelable
