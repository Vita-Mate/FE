package com.my.vitamateapp.Challenge

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.my.vitamateapp.HomeActivity
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.ActivityChallengeCreateIndividualBinding

class ChallengeCreateIndividualActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChallengeCreateIndividualBinding
    private lateinit var category: String
    private var selectedDuration: String? = null

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
            navigateToNextActivity() // 다음 액티비티로 이동
        }
    }

    private fun logInputData() {
        val year = binding.startYear.text.toString()
        val month = binding.startMonth.text.toString()
        val date = binding.startDate.text.toString()
        val frequency = binding.weekFreqency.text.toString()
        val description = binding.description.text.toString()

        Log.d("ChallengeCreateIndividualActivity", "Start Date: $year-$month-$date")
        Log.d("ChallengeCreateIndividualActivity", "Weekly Frequency: $frequency")
        Log.d("ChallengeCreateIndividualActivity", "Challenge Message: $description")
        Log.d("ChallengeCreateIndividualActivity", "Duration Selected: $selectedDuration") // 이 부분에서 로그를 확인
    }

    private fun navigateToNextActivity() {
        val year = binding.startYear.text.toString()
        val month = binding.startMonth.text.toString()
        val date = binding.startDate.text.toString()
        val frequency = binding.weekFreqency.text.toString().toInt()
        val description = binding.description.text.toString()

        // Intent에 데이터 추가
        val intent = Intent(this, HomeActivity::class.java).apply {
            putExtra("category", category) // 카테고리 저장
            putExtra("startDate", "$year-$month-$date") // 시작 날짜 저장
            putExtra("weeklyFrequency", frequency) // 주간 빈도 저장
            putExtra("duration", selectedDuration) // 선택한 기간 저장
            putExtra("description", description) // 설명 저장
        }
        // Toast로 생성 완료 알림 표시
        Toast.makeText(this, "챌린지가 성공적으로 생성되었습니다.", Toast.LENGTH_SHORT).show()

        startActivity(intent)
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
            Log.d("ChallengeCreateIndividualActivity", "Selection cleared.")
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
            R.id.duration_one_week -> "ONE_WEEK"
            R.id.duration_one_month -> "ONE_MONTH"
            R.id.duration_three_months -> "THREE_MONTHS"
            R.id.duration_six_months -> "SIX_MONTHS"
            R.id.duration_one_year -> "ONE_YEAR"
            else -> null
        }

        // 현재 선택된 뷰 업데이트
        selectedDurationView = selectedView

        Log.d("ChallengeCreateIndividualActivity", "Selected duration: $selectedDuration")
    }
}
