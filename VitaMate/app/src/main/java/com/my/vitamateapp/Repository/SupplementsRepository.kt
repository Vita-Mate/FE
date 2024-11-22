package com.my.vitamateapp.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.my.vitamateapp.Api.GetSupplementsDetailApi
import com.my.vitamateapp.Api.GetTakingSupplementsApi
import com.my.vitamateapp.Api.RetrofitInstance
import com.my.vitamateapp.Api.SupplementsTakingApi
import com.my.vitamateapp.Api.SupplementReviewApi
import com.my.vitamateapp.Api.SupplementsScrapApi
import com.my.vitamateapp.Api.SupplementsSimulationApi
import com.my.vitamateapp.mySupplement.AddedSupplementModel
import com.my.vitamateapp.mySupplement.GetDetailResponse
import com.my.vitamateapp.mySupplement.NutrientInfo
import com.my.vitamateapp.mySupplement.ReviewItem
import com.my.vitamateapp.mySupplement.ScrapResult
import com.my.vitamateapp.mySupplement.WriteReviewRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class SupplementsRepository(private val context: Context) {
    //섭취 영양제 조회 api
    suspend fun getTakingSupplements(accessToken: String, page: Int = 0): List<AddedSupplementModel> {
        val apiService = RetrofitInstance.getInstance().create(GetTakingSupplementsApi::class.java)

        return try {
            val response = apiService.getTakingSupplements("Bearer $accessToken", page)
            if (response.isSuccss) {
                response.result.supplementList.map { supplement ->
                    AddedSupplementModel(
                        id = supplement.supplementId,
                        name = supplement.name,
                        duration = supplement.duration
                    )
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                }
                emptyList()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "네트워크 오류: ${e.message}", Toast.LENGTH_SHORT).show()
            }
            emptyList()
        }
    }

    //섭취영양제 삭제 api
    suspend fun deleteSupplement(accessToken: String, supplementId: Int): Boolean {
        val apiService = RetrofitInstance.getInstance().create(SupplementsTakingApi::class.java)

        return try {
            val response = apiService.deleteTakingSupplements("Bearer $accessToken", supplementId)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                }
                true
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "삭제 실패: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
                false
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "네트워크 오류: ${e.message}", Toast.LENGTH_SHORT).show()
            }
            false
        }
    }


    // 영양제 리뷰 작성 API
    suspend fun writeSupplementReview(accessToken: String, supplementId: Int, reviewRequest: WriteReviewRequest): Boolean {
        val apiService = RetrofitInstance.getInstance().create(SupplementReviewApi::class.java)

        return try {
            // API 호출
            val response = apiService.writeSupplementsReview("Bearer $accessToken", supplementId, reviewRequest)

            // 응답이 성공적인지 확인
            if (response.isSuccess || response.code == "COMMON200") {
                withContext(Dispatchers.Main) {
                }
                true // 성공 시 true 반환
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "리뷰 작성 실패: ${response.message}", Toast.LENGTH_SHORT).show()
                }
                false // 실패 시 false 반환
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "네트워크 오류: ${e.message}", Toast.LENGTH_SHORT).show()
            }
            false // 예외 발생 시 false 반환
        }
    }

    // 영양제 리뷰 조회 API
    suspend fun getSupplementReviews(accessToken: String, supplementId: Int): List<ReviewItem>? {
        val apiService = RetrofitInstance.getInstance().create(SupplementReviewApi::class.java)
        val page = 0 // 기본 페이지 번호
        val pageSize = 15 // 기본 페이지 크기

        return try {
            // API 호출
            val response = apiService.getSupplementReviews("Bearer $accessToken", supplementId, page, pageSize)

            // 응답이 성공적일 경우 리뷰 리스트 반환
            if (response.isSuccess || response.code == "COMMON200") {
                response.result.reviewList // 리뷰 리스트 반환
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "리뷰 조회 실패: ${response.message}", Toast.LENGTH_SHORT).show()
                }
                null // 실패 시 null 반환
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "네트워크 오류: ${e.message}", Toast.LENGTH_SHORT).show()
            }
            null // 예외 발생 시 null 반환
        }
    }

    // 영양제 상세 정보 조회API
    suspend fun getSupplementsDetail(accessToken: String, supplementId: Int): GetDetailResponse? {
        val apiService = RetrofitInstance.getInstance().create(GetSupplementsDetailApi::class.java)

        return try {
            // API 호출
            val response = apiService.getSupplementsDetail("Bearer $accessToken", supplementId)

            // 응답이 성공적일 경우 전체 응답을 반환
            if (response.isSuccss && response.code == "COMMON200") {
                response // GetDetailResponse 반환
            } else {
                Log.e("SupplementDetail", "상세 정보 조회 실패: ${response.message}")
                null
            }
        } catch (e: Exception) {
            Log.e("SupplementDetail", "네트워크 오류: ${e.message}")
            null
        }
    }

    // 스크랩 추가 API
    suspend fun addSupplementScrap(accessToken: String, supplementId: Int): Boolean {
        val apiService = RetrofitInstance.getInstance().create(SupplementsScrapApi::class.java)

        return try {
            val response = apiService.scrapSupplements("Bearer $accessToken", supplementId)

            // 응답 상태 코드 및 본문 확인
            if (response.isSuccessful) {
                val body = response.body()
                Log.d("SupplementScrap", "응답 본문: $body")
                if (body?.isSuccss == true) {
                    Log.d("SupplementScrap", "스크랩 추가 성공: ${body.result}")
                    true
                } else {
                    Log.e("SupplementScrap", "스크랩 추가 실패: ${body?.message}")
                    false
                }
            } else {
                Log.e("SupplementScrap", "응답 실패: ${response.errorBody()?.string()}")
                false
            }
        } catch (e: Exception) {
            Log.e("SupplementScrap", "스크랩 추가 네트워크 오류: ${e.message}")
            false
        }
    }

    // 스크랩삭제 API
    suspend fun deleteSupplementScrap(accessToken: String, supplementId: Int): Boolean {
        val apiService = RetrofitInstance.getInstance().create(SupplementsScrapApi::class.java)

        return try {
            val response = apiService.deleteScrapSupplements("Bearer $accessToken", supplementId)

            // 응답 상태 코드 및 본문 확인
            if (response.isSuccessful) {
                val body = response.body()
                Log.d("SupplementScrap", "응답 본문: $body")
                if (body?.isSuccss == true) {
                    Log.d("SupplementScrap", "스크랩 삭제 성공: ${body.result}")
                    true
                } else {
                    Log.e("SupplementScrap", "스크랩 삭제 실패: ${body?.message}")
                    false
                }
            } else {
                Log.e("SupplementScrap", "응답 실패: ${response.errorBody()?.string()}")
                false
            }
        } catch (e: Exception) {
            Log.e("SupplementScrap", "스크랩 삭제 네트워크 오류: ${e.message}")
            false
        }
    }

    // 스크랩목록 조회 API
    suspend fun getScrapSupplements(accessToken: String): List<ScrapResult> {
        val apiService = RetrofitInstance.getInstance().create(SupplementsScrapApi::class.java)

        return try {
            val response = apiService.getScrapSupplements("Bearer $accessToken")
            if (response.isSuccss) {
                response.result
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                }
                emptyList()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "네트워크 오류: ${e.message}", Toast.LENGTH_SHORT).show()
            }
            Log.e("SupplementScrap", "스크랩 목록 조회 네트워크 오류: ${e.message}")
            emptyList()
        }
    }


    // 영양소 조회 API
    suspend fun getTakingNutrients(accessToken: String, page: Int = 0, pageSize: Int = 10): List<NutrientInfo> {
        val apiService = RetrofitInstance.getInstance().create(SupplementsTakingApi::class.java)

        return try {
            val response = apiService.getSupplementNutrients("Bearer $accessToken", page, pageSize)
            if (response.isSuccss) {
                response.result.intakeNutrientList
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                }
                emptyList()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "네트워크 오류: ${e.message}", Toast.LENGTH_SHORT).show()
            }
            Log.e("SupplementsRepository", "영양소 조회 네트워크 오류: ${e.message}")
            emptyList()
        }
    }

    // 섭취중인 영양제 + 스크랩한 영양제 영양소 합산 API 호출 함수
    suspend fun getSimulatedSupplementsNutrients(accessToken: String): List<NutrientInfo>? {
        val apiService = RetrofitInstance.getInstance().create(SupplementsSimulationApi::class.java)

        return try {
            val response = apiService.simulatioSupplements("Bearer $accessToken")

            // 응답 처리: 성공 시 영양소 리스트 반환
            if (response.isSuccss) {
                response.result // NutrientInfo 리스트 반환
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "오류: ${response.message}", Toast.LENGTH_SHORT).show()
                }
                Log.e("SupplementsRepository", "API 호출 실패: ${response.message}")
                null
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "네트워크 오류: ${e.message}", Toast.LENGTH_SHORT).show()
            }
            Log.e("SupplementsRepository", "영양소 합산 네트워크 오류: ${e.message}")
            null // 예외 발생 시 null 반환
        }
    }
}
