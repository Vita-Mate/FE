package com.my.vitamateapp

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.kakao.sdk.user.UserApiClient
import com.my.vitamateapp.databinding.ActivityHomeBinding
import com.my.vitamateapp.registerPage.MainActivity

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)


        // 로그아웃 버튼 클릭 시 로그아웃
        binding.kakaoLogoutButton.setOnClickListener {
            vitamate_logout()
        }

        // 계정탈퇴 버튼 클릭 시 계정삭제
        binding.deleteAccountButton.setOnClickListener {
            deleteAccount()
        }
    }

    /* 함수 구현 */
    private fun vitamate_logout() {
        // 로그아웃
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Log.e(ContentValues.TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
            } else {
                Log.i(ContentValues.TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
                startActivity(Intent(this, MainActivity::class.java))
                finish() // 로그아웃 성공하면 다시 앱 초기 화면으로 이동.
            }
        }
    }

    private fun deleteAccount() {
        // 계정삭제
        UserApiClient.instance.unlink { error ->
            if (error != null) {
                Log.e(ContentValues.TAG, "연결 끊기 실패", error)
            } else {
                Log.i(ContentValues.TAG, "연결 끊기 성공. SDK에서 토큰 삭제 됨")
                startActivity(Intent(this, MainActivity::class.java))
                finish() // 계정 삭제 성공하면 다시 앱 초기 화면으로 이동.
            }
        }
    }
}