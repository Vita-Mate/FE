package com.my.vitamateapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.kakao.sdk.user.UserApiClient
import com.my.vitamateapp.Challenge.ChallengeSelectModeActivity
import com.my.vitamateapp.databinding.ActivityHomeBinding
import com.my.vitamateapp.registerPage.MainActivity

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private var selectedCategory: String? = null  // 선택된 카테고리 저장
    private var challengeId: Long? = null  // 챌린지 ID를 저장

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Intent에서 데이터 수신
        val challengeCreated = intent.getBooleanExtra("challengeCreated", false)
        val message = intent.getStringExtra("message")


        if (challengeCreated) {
            // 성공적으로 챌린지가 생성된 경우 사용자에게 알림
            Toast.makeText(this, message ?: "챌린지가 생성되었습니다.", Toast.LENGTH_SHORT).show()
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)



        // 버튼 클릭 리스너 설정
        binding.homeHealthBtn.setOnClickListener {
            selectedCategory = "EXERCISE"  // 운동 선택
            challengeId = getChallengeIdByCategory(selectedCategory)
            goSelectmode()
        }

        binding.homeDrunkBtn.setOnClickListener {
            selectedCategory = "QUIT_ALCOHOL"  // 금주 선택
            challengeId = getChallengeIdByCategory(selectedCategory)
            goSelectmode()
        }

        binding.homeSmokeBtn.setOnClickListener {
            selectedCategory = "QUIT_SMOKE"  // 금연 선택
            challengeId = getChallengeIdByCategory(selectedCategory)
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



    private fun getChallengeIdByCategory(category: String?): Long {
        val sharedPref = getSharedPreferences("ChallengePreferences", Context.MODE_PRIVATE)
        return if (category != null) {
            sharedPref.getLong("challengeId_$category", -1L) // 기본값 -1L
        } else {
            -1L
        }
    }

    private fun goSelectmode() {
        if (selectedCategory == null) {
            showToast("카테고리가 선택되지 않았습니다.")
            return
        }

        // Intent 생성 및 데이터 전달
        val intent = Intent(this, ChallengeSelectModeActivity::class.java).apply {
            putExtra("category", selectedCategory)  // 선택된 카테고리
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

    private fun getAccessToken(context: Context): String? {
        return context.getSharedPreferences("saved_user_info", Context.MODE_PRIVATE)
            .getString("accessToken", null)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
