package com.my.vitamateapp.Api

import com.my.vitamateapp.mySupplement.DeleteTakingSupplementResponse
import com.my.vitamateapp.mySupplement.TakingSupplementModel
import com.my.vitamateapp.mySupplement.TakingSupplementResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface SupplementsTakingApi {

    // 복용할 영양제 추가 API
    @POST("supplements/{supplementId}/taking")
    suspend fun takingSupplements(
        @Header("Authorization") accessToken: String,
        @Path("supplementId") supplementId: Int,
        @Body requestBody: TakingSupplementModel // 추가
    ): Response<TakingSupplementResponse>

    // 복용 중인 영양제 삭제 API
    @DELETE("supplements/{supplementId}/taking")
    suspend fun deleteTakingSupplements(
        @Header("Authorization") accessToken: String,
        @Path("supplementId") supplementId: Int
    ): Response<DeleteTakingSupplementResponse>
}

