package com.my.vitamateapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kakao.sdk.user.UserApiClient
import com.my.vitamateapp.databinding.ActivityUserPageBinding
import com.my.vitamateapp.registerPage.MainActivity

class UserPageActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUserPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // binding 초기화
        binding = ActivityUserPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 계정탈퇴 버튼 클릭 시 계정삭제
        binding.kakaoLogoutButton.setOnClickListener {
            deleteAccount()
        }
    }

    // 로그아웃 기능
    private fun vitamate_logout() {
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Log.e("HomeActivity", "로그아웃 실패", error)
            } else {
                Log.i("HomeActivity", "로그아웃 성공")
                startActivity(Intent(this, MainActivity::class.java))
                finish() // 로그아웃 성공하면 다시 앱 초기 화면으로 이동.
            }
        }
    }

    // 계정 삭제 기능
    private fun deleteAccount() {
        UserApiClient.instance.unlink { error ->
            if (error != null) {
                Log.e("HomeActivity", "연결 끊기 실패", error)
            } else {
                Log.i("HomeActivity", "연결 끊기 성공")
                startActivity(Intent(this, MainActivity::class.java))
                finish() // 계정 삭제 성공하면 다시 앱 초기 화면으로 이동.
            }
        }
    }
}