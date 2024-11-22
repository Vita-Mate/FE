package com.my.vitamateapp.Challenge

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.my.vitamateapp.Api.RetrofitInstance
import com.my.vitamateapp.Api.PersonalCreateChallengeApi
import com.my.vitamateapp.ChallengeDTO.CreateChallengePersonalRequest
import com.my.vitamateapp.ChallengeDTO.CreateChallengePersonalResponse
import com.my.vitamateapp.ChallengeDTO.IndCategory
import com.my.vitamateapp.ChallengeDTO.IndDuration
import com.my.vitamateapp.HomeActivity
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.ActivityChallengeCreateIndividualBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChallengeCreatePersonalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChallengeCreateIndividualBinding
    private lateinit var category: String
    private var selectedDuration: IndDuration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Data Binding 설정
        binding = DataBindingUtil.setContentView(this, R.layout.activity_challenge_create_individual)

        // Intent로 데이터 수신
        val categoryString = intent.getStringExtra("category") ?: return
        category = categoryString

        // 버튼 클릭 리스너 설정
        binding.preButton1.setOnClickListener { finish() }

        // 기간 선택 버튼 설정
        binding.durationOneWeek.setOnClickListener { selectDuration(it.id) }
        binding.durationOneMonth.setOnClickListener { selectDuration(it.id) }
        binding.durationThreeMonths.setOnClickListener { selectDuration(it.id) }
        binding.durationSixMonths.setOnClickListener { selectDuration(it.id) }
        binding.durationOneYear.setOnClickListener { selectDuration(it.id) }

        binding.submitButton.setOnClickListener {
            logInputData() // 입력 데이터 로그 출력
            createChallenge() // 서버로 챌린지 생성 요청
        }
    }

    private fun logInputData() {
        val year = binding.startYear.text.toString()
        val month = binding.startMonth.text.toString()
        val date = binding.startDate.text.toString()
        val frequency = binding.weekFreqency.text.toString()

        Log.d("ChallengeCreateIndividualActivity", "Start Date: $year-$month-$date")
        Log.d("ChallengeCreateIndividualActivity", "Weekly Frequency: $frequency")
        Log.d("ChallengeCreateIndividualActivity", "Duration Selected: $selectedDuration") // 이 부분에서 로그를 확인
    }

    private fun createChallenge() {
        val year = binding.startYear.text.toString()
        val month = binding.startMonth.text.toString()
        val date = binding.startDate.text.toString()
        val frequency = binding.weekFreqency.text.toString().toInt()

        // Access Token 가져오기
        val accessToken = getAccessToken()
        if (accessToken == null) {
            showToast("Access Token이 없습니다. 로그인을 다시 시도하세요.")
            return
        }else Log.i(TAG, "액세스 토큰: ${accessToken}")

        // Create challenge request
        val indCategory = IndCategory.values().find { it.name == category } ?: run {
            showToast("유효한 카테고리가 아닙니다.")
            return
        }

        // duration이 null일 경우 기본값을 설정
        val duration = selectedDuration ?: IndDuration.ONE_WEEK

        val request = CreateChallengePersonalRequest(
            category = indCategory,
            startDate = "$year-$month-$date",
            duration = duration,
            weeklyFrequency = frequency,
        )

        val apiService = RetrofitInstance.getInstance().create(PersonalCreateChallengeApi::class.java)
        apiService.createPersonalChallenge("Bearer $accessToken", request)
            .enqueue(object : Callback<CreateChallengePersonalResponse> {
                override fun onResponse(
                    call: Call<CreateChallengePersonalResponse>,
                    response: Response<CreateChallengePersonalResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.d("ChallengeCreatePersonalActivity", "Challenge created successfully.")
                        showToast("챌린지가 성공적으로 생성되었습니다.")
                        navigateToHomeActivity() // 생성 후 홈 화면으로 이동
                    } else {
                        val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                        Log.e("ChallengeCreatePersonalActivity", "API Error: $errorMessage")
                        showToast("서버 에러 발생: $errorMessage")
                    }
                }

                override fun onFailure(call: Call<CreateChallengePersonalResponse>, t: Throwable) {
                    Log.e("ChallengeCreatePersonalActivity", "Network error: ${t.localizedMessage}")
                    showToast("네트워크 에러: ${t.localizedMessage}")
                }
            })
    }

    private fun navigateToHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // SharedPreferences에서 Access Token 가져오기
    private fun getAccessToken(): String? {
        val sharedPref = getSharedPreferences("saved_user_info", MODE_PRIVATE)
        return sharedPref.getString("accessToken", null)
    }

    private var selectedDurationView: View? = null

    private fun selectDuration(selectedId: Int) {
        val selectedView = findViewById<View>(selectedId)

        // 현재 선택된 버튼을 다시 클릭할 경우 선택 해제
        if (selectedView == selectedDurationView) {
            selectedDurationView?.isSelected = false
            selectedDurationView?.setBackgroundResource(R.drawable.button_background)
            selectedDurationView = null
            selectedDuration = null
            Log.d("ChallengeCreatePersonalActivity", "Selection cleared.")
            return
        }

        // 이전에 선택된 뷰가 있다면 기본 색상으로 되돌림
        selectedDurationView?.isSelected = false
        selectedDurationView?.setBackgroundResource(R.drawable.button_background)

        // 새로 선택된 뷰에 선택된 색상 유지
        selectedView.isSelected = true
        selectedView.setBackgroundResource(R.drawable.button_selected_background) // 선택된 상태 색상

        // 선택된 기간 설정
        selectedDuration = when (selectedId) {
            R.id.duration_one_week -> IndDuration.ONE_WEEK
            R.id.duration_one_month -> IndDuration.ONE_MONTH
            R.id.duration_three_months -> IndDuration.THREE_MONTHS
            R.id.duration_six_months -> IndDuration.SIX_MONTHS
            R.id.duration_one_year -> IndDuration.ONE_YEAR
            else -> null
        }

        // 현재 선택된 뷰 업데이트
        selectedDurationView = selectedView

        Log.d("ChallengeCreatePersonalActivity", "Selected duration: $selectedDuration")
    }
}
