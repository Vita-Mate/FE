package com.my.vitamateapp.Challenge


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.ActivityChallengeCreateGroupObjectiveBinding
import java.text.SimpleDateFormat
import java.util.*

class ChallengeCreateGroupObjectiveActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChallengeCreateGroupObjectiveBinding
    private lateinit var category: Category // Store category here
    private lateinit var title: String // Store title here
    private lateinit var description: String // Store description here
    private var selectedDuration: Duration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up Data Binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_challenge_create_group_objective)

        // Get data from Intent
        val categoryString = intent.getStringExtra("category") ?: return
        title = intent.getStringExtra("title") ?: return
        description = intent.getStringExtra("description") ?: return

        // Convert the category string to the enum
        category = Category.valueOf(categoryString)

        // Set up click listeners using binding
        binding.preButton1.setOnClickListener { finish() }
        binding.groupPreButton.setOnClickListener { goGroupName() }
        binding.groupNextButton.setOnClickListener { createGroupPeople() }

        binding.durationOneWeek.setOnClickListener { selectDuration(it.id) }
        binding.durationOneMonth.setOnClickListener { selectDuration(it.id) }
        binding.durationThreeMonths.setOnClickListener { selectDuration(it.id) }
        binding.durationSixMonths.setOnClickListener { selectDuration(it.id) }
        binding.durationOneYear.setOnClickListener { selectDuration(it.id) }
    }

    private fun goGroupName() {
        val intent = Intent(this, ChallengeCreateGroupActivity::class.java)
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
            Log.d("ChallengeCreateGroupObjectiveActivity", "Selection cleared.")
            return
        }

        // 이전에 선택된 뷰가 있다면 기본 색상으로 되돌림
        selectedDurationView?.isSelected = false
        selectedDurationView?.setBackgroundResource(R.drawable.button_background)

        // 새 선택된 뷰 설정 및 선택 색상 유지
        selectedView.isSelected = true
        selectedView.setBackgroundResource(R.drawable.button_selected_background) // 선택된 상태 유지 색상 설정

        // 선택된 기간 설정
        selectedDuration = when (selectedId) {
            R.id.duration_one_week -> Duration.ONE_WEEK
            R.id.duration_one_month -> Duration.ONE_MONTH
            R.id.duration_three_months -> Duration.THREE_MONTHS
            R.id.duration_six_months -> Duration.SIX_MONTHS
            R.id.duration_one_year -> Duration.ONE_YEAR
            else -> null
        }

        // 현재 선택된 뷰 업데이트
        selectedDurationView = selectedView

        Log.d("ChallengeCreateGroupObjectiveActivity", "Selected duration: $selectedDuration")
    }





    private fun createGroupPeople() {
        val year = binding.startYear.text.toString()
        val month = binding.startMonth.text.toString()
        val date = binding.startDate.text.toString()
        val weekFrequency = binding.weekFreqency.text.toString().toIntOrNull()
        if (weekFrequency == null) {
            showToast("주당 빈도를 정확히 입력하세요.")
            return
        }
        // Check for empty date fields
        if (year.isEmpty() || month.isEmpty() || date.isEmpty()) {
            showToast("날짜를 정확히 입력하세요.")
            return
        }

        val startDate = "$year-$month-$date"

        // Validate the date format
        if (!isValidDate(startDate)) {
            showToast("유효하지 않은 날짜 형식입니다.")
            return
        }


        // Ensure duration is selected
        val duration = selectedDuration
        if (duration == null) {
            showToast("기간을 선택하세요.")
            Log.d("ChallengeCreateGroupObjectiveActivity", "Duration not selected or invalid.")
            return
        }

        Log.d("ChallengeCreateGroupObjectiveActivity", "Selected duration: $duration")

        // Pass data to the next activity
        val intent = Intent(this, ChallengeCreateGroupPeopleActivity::class.java).apply {
            putExtra("category", category.name) // Pass the category as a string
            putExtra("title", title) // Pass title
            putExtra("description", description) // Pass description
            putExtra("startDate", startDate)
            putExtra("weekFrequency", weekFrequency)
            putExtra("duration", duration.name)
        }
        startActivity(intent)
    }

    private fun isValidDate(date: String): Boolean {
        return try {
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            format.isLenient = false
            format.parse(date) != null
        } catch (e: Exception) {
            false
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
