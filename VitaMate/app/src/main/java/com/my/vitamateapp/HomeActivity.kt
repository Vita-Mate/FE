package com.my.vitamateapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.kakao.sdk.user.UserApiClient
import com.my.vitamateapp.Challenge.ChallengeSelectModeActivity
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.ActivityHomeBinding
import com.my.vitamateapp.registerPage.MainActivity

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private var selectedCategory: String? = null  // 선택된 카테고리 저장
    private var challengeId: Long? = null  // 챌린지 ID를 저장

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        // SharedPreferences에서 challengeId 읽기
        challengeId = getChallengeIdFromPreferences()

        // 버튼 클릭 리스너 설정
        binding.homeHealthBtn.setOnClickListener {
            selectedCategory = "EXERCISE"  // 운동 선택
            goSelectmode()
        }

        binding.homeDrunkBtn.setOnClickListener {
            selectedCategory = "QUIT_ALCOHOL"  // 금주 선택
            goSelectmode()
        }

        binding.homeSmokeBtn.setOnClickListener {
            selectedCategory = "QUIT_SMOKE"  // 금연 선택
            goSelectmode()
        }

        binding.kakaoLogoutButton.setOnClickListener {
            vitamate_logout()
        }

        binding.deleteAccountButton.setOnClickListener {
            deleteAccount()
        }

        binding.clearSharedPreferences.setOnClickListener {
            clearAllSharedPreferences()  // SharedPreferences 초기화
        }
    }

    // SharedPreferences에서 challengeId 읽어오는 함수
    private fun getChallengeIdFromPreferences(): Long {
        val sharedPref = getSharedPreferences("ChallengePreferences", Context.MODE_PRIVATE)
        val challengeId = sharedPref.getLong("challengeId", -1L)  // 저장된 challengeId를 가져옴, 없으면 -1L 반환
        Log.d("HomeActivity", "저장된 challengeId: $challengeId")  // 로그로 값 확인
        return challengeId
    }

    // challengeId와 category를 전달하는 함수
    private fun goSelectmode() {
        val intent = Intent(this, ChallengeSelectModeActivity::class.java).apply {
            selectedCategory?.let { putExtra("category", it) }  // Category 이름 전달
            challengeId?.let { putExtra("challengeId", it) }  // challengeId가 있으면 전달
            Log.d("HomeActivity", "challengeId: $challengeId, Category: $selectedCategory")
        }
        startActivity(intent)
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

    // SharedPreferences 초기화 기능
    private fun clearAllSharedPreferences() {
        // "ChallengePreferences" 초기화
        val challengePref = getSharedPreferences("ChallengePreferences", Context.MODE_PRIVATE)
        val challengeEditor = challengePref.edit()
        challengeEditor.clear()
        challengeEditor.apply()
        Log.d("SharedPreferences", "모든 SharedPreferences 초기화 완료")
    }
}
