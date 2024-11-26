package com.my.vitamateapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        // 사용자 닉네임 설정
        displayNickname()

        // Intent에서 데이터 수신
        val challengeCreated = intent.getBooleanExtra("challengeCreated", false)
        val message = intent.getStringExtra("message")


        if (challengeCreated) {
            // 성공적으로 챌린지가 생성된 경우 사용자에게 알림
            Toast.makeText(this, message ?: "챌린지가 생성되었습니다.", Toast.LENGTH_SHORT).show()
        }

        // 사용자 아이콘 클릭시 userPage로 이동
        binding.goUserPage.setOnClickListener{
            val intent = Intent(this, UserPageActivity::class.java)
            startActivity(intent)
        }

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

        binding.clearSharedPreferences.setOnClickListener {
            clearAllSharedPreferences()  // SharedPreferences 초기화
        }
    }



    private fun getChallengeIdByCategory(category: String?): Long {
        val sharedPref = getSharedPreferences("ChallengePref", Context.MODE_PRIVATE)
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

    // 사용자 닉네임 보여주기 함수
    private fun displayNickname() {
        // SharedPreferences에서 닉네임 가져오기
        val sharedPreferences = getSharedPreferences("saved_user_info", Context.MODE_PRIVATE)
        val userNickname = sharedPreferences.getString("nickname", "사용자") ?: "사용자"

        // 로그로 저장된 닉네임 출력
        Log.d("NicknameLog", "저장된 닉네임: $userNickname")

        // 닉네임 TextView에 값 설정
        val nicknameTextView: TextView = findViewById(R.id.nickname)
        nicknameTextView.text = "$userNickname"
    }

    // SharedPreferences 초기화 기능
    private fun clearAllSharedPreferences() {
        // "ChallengePreferences" 초기화
        val sharedPref = getSharedPreferences("ChallengePrefs", Context.MODE_PRIVATE)
        val challengeEditor = sharedPref.edit()
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
