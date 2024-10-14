package com.my.vitamateapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.kakao.sdk.user.UserApiClient
import com.my.vitamateapp.Challenge.Category
import com.my.vitamateapp.Challenge.ChallengeSelectModeActivity
import com.my.vitamateapp.Challenge.CreateChallengeIndividual
import com.my.vitamateapp.databinding.ActivityHomeBinding
import com.my.vitamateapp.registerPage.MainActivity

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private var selectedCategory: Category? = null  // 선택된 카테고리 저장

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        // Intent로부터 데이터 수신
        val category = intent.getStringExtra("category")
        val startDate = intent.getStringExtra("startDate")
        val weeklyFrequency = intent.getIntExtra("weeklyFrequency", 0)
        val duration = intent.getStringExtra("duration")
        val description = intent.getStringExtra("description")

        // 수신한 데이터 로그 출력 (테스트 용)
        Log.d("HomeActivity", "Category: $category")
        Log.d("HomeActivity", "Start Date: $startDate")
        Log.d("HomeActivity", "Weekly Frequency: $weeklyFrequency")
        Log.d("HomeActivity", "Duration: $duration")
        Log.d("HomeActivity", "Description: $description")

        val challenge = intent.getSerializableExtra("challenge") as? CreateChallengeIndividual
        challenge?.let {
            Log.d("HomeActivity", "챌린지 생성됨: ${it.category}, ${it.startDate}, ${it.weeklyFrequency}, ${it.duration}, ${it.description}")
        }

        // 로그아웃 버튼 클릭 시 로그아웃
        binding.kakaoLogoutButton.setOnClickListener {
            vitamate_logout()
        }

        // 계정탈퇴 버튼 클릭 시 계정삭제
        binding.deleteAccountButton.setOnClickListener {
            deleteAccount()
        }

        // 카테고리 버튼 설정
        setupCategoryButtons()
    }

    private fun setupCategoryButtons() {
        binding.homeHealthBtn.setOnClickListener {
            selectedCategory = Category.EXERCISE  // 운동 선택
            startChallenge()
        }

        binding.homeDrunkBtn.setOnClickListener {
            selectedCategory = Category.QUIT_ALCOHOL  // 금주 선택
            startChallenge()
        }

        binding.homeSmokeBtn.setOnClickListener {
            selectedCategory = Category.QUIT_SMOKE  // 금연 선택
            startChallenge()
        }
    }

    private fun startChallenge() {
        if (selectedCategory == null) {
            showAlertDialog("카테고리를 선택하세요.")
            return
        }

        val intent = Intent(this, ChallengeSelectModeActivity::class.java).apply {
            putExtra("category", selectedCategory?.name) // 선택된 카테고리 전달
        }
        startActivity(intent)
    }

    private fun showAlertDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("알림")
        builder.setMessage(message)
        builder.setPositiveButton("확인") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

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
