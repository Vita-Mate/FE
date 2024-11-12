package com.my.vitamateapp.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.my.vitamateapp.Api.CheckNicknameApi
import com.my.vitamateapp.Api.RetrofitInstance

class MembersRepository(private val context: Context) {

    // 닉네임 중복 확인 API
    suspend fun checkNicknameDuplicate(nickname: String): Boolean {
        val apiService = RetrofitInstance.getInstance().create(CheckNicknameApi::class.java)

        return try {
            val response = apiService.checkNickname(nickname)

            if (response.isSuccessful && response.body() != null) {
                val nicknameResponse = response.body()!!

                Log.d("MembersRepository", "API 응답 코드: ${response.code()}")

                if (nicknameResponse.isSuccss) {
                    Log.d("MembersRepository", "중복 확인 성공: ${nicknameResponse.result}")
                    true // 중복되지 않음
                } else {
                    Log.d("MembersRepository", "중복 확인 실패: ${nicknameResponse.message}")
                    false // 중복됨
                }
            } else {
                Log.e("MembersRepository", "응답 실패: ${response.code()}")
                false // 요청 실패
            }
        } catch (e: Exception) {
            Log.e("MembersRepository", "중복 확인 요청 실패: 예외 발생", e)
            false // 요청 실패
        }
    }
}
