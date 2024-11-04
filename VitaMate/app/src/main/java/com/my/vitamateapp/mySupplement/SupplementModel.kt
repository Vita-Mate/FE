package com.my.vitamateapp.mySupplement

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

//SupplementsSearchApi
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
    val result: SearchResult
) : Parcelable

// 결과 모델
@Parcelize
data class SearchResult(
    val supplementList: List<SearchedSupplementModel>,
    val listSize: Int,
    val totalPage: Int,
    val totalElements: Int,
    val isFirst: Boolean,
    val isLast: Boolean
) : Parcelable

//GetTakingSupplementsApi
// 섭취 중인 영양제 API 응답 모델
@Parcelize
data class GetTakingSupplementsResponse(
    val isSuccss: Boolean, // 오타 수정
    val code: String,
    val message: String,
    val result: GetTakingResult
) : Parcelable

// 섭취 중인 영양제 결과 모델
@Parcelize
data class GetTakingResult(
    val supplementList: List<GetTakingSupplementModel>,
    val listSize: Int,
    val totalPage: Int,
    val totalElements: Int,
    val isFirst: Boolean,
    val isLast: Boolean
) : Parcelable

// 섭취 중인 영양제 모델
@Parcelize
data class GetTakingSupplementModel(
    val supplementId: Int,
    val name: String,
    val brand: String,
    val duration: Int // 복용 기간
) : Parcelable

//SupplementsTakingApi
//추가한 영양제 모델
@Parcelize
data class TakingSupplementModel(
    val startDate: String // 시작 날짜
) : Parcelable
@Parcelize
data class TakingSupplementResponse(
    val isSuccss: Boolean, // 성공 여부
    val code: String, // 상태 코드
    val message: String, // 메시지
    val result: TakingResult, // 결과 데이터
    val success: Boolean // 성공 여부
) : Parcelable

// 결과 모델
@Parcelize
data class TakingResult(
    val supplementID: Int,          // 영양제 ID 추가
    val supplementName: String,     // 영양제 이름
    val supplementBrand: String,     // 영양제 브랜드
    val duration: Int                // 복용 기간
) : Parcelable

//추가한 영양제 삭제 API
data class DeleteTakingSupplementResponse(
    val isSuccss: Boolean,
    val code: String,
    val message: String,
    val result: DeleteResult,
    val success: Boolean
)

data class DeleteResult(
    val supplementID: Int,
    val supplementName: String,
    val supplementBrand: String
)

// 영양제 상세 정보 조회 API
@Parcelize
data class GetDetailResponse(
    val isSuccss: Boolean,
    val code: String,
    val message: String,
    val result: SupplementDetailResult,
    val success: Boolean
) : Parcelable

// 상세 정보 결과 모델
@Parcelize
data class SupplementDetailResult(
    val supplementId: Int,
    val brand: String,
    val name: String,
    val nutrientInfoImageUrl: String?,
    val isScrapped: Boolean,
    val reviewList: List<SupplementReview>,
    val recommendList: List<RecommendedSupplement>
) : Parcelable

// 리뷰 정보 모델
@Parcelize
data class SupplementReview(
    val reviewId: Int,
    val nickname: String,
    val content: String,
    val grade: Int,
    val createdDate: String
) : Parcelable

// 추천 영양제 정보 모델
@Parcelize
data class RecommendedSupplement(
    val supplementId: Int,
    val brand: String,
    val name: String,
    val imageURL: String?,
    val isScrapped: Boolean
) : Parcelable