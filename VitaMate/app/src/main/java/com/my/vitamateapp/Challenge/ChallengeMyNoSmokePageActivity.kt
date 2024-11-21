package com.my.vitamateapp.Challenge

import AddQuitChallengeRecordApi
import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.my.vitamateapp.Api.RetrofitInstance
import com.my.vitamateapp.ChallengeDTO.ChallengePreviewResponse
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.ActivityChallengeMyNoDrinkPageBinding
import com.my.vitamateapp.network.ChallengeJoinResultApi
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ChallengeMyNoSmokePageActivity : AppCompatActivity() {
    private var selectedDate: CalendarDay? = null
    private lateinit var binding: ActivityChallengeMyNoDrinkPageBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var challengeId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 바인딩 초기화
        binding = DataBindingUtil.setContentView(this, R.layout.activity_challenge_my_no_smoke_page)
        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

        // savedInstanceState로부터 challengeId 복원
        challengeId = savedInstanceState?.getLong("challengeId")
            ?: intent.getLongExtra("challengeId", -1L)

        // 전달된 challengeId 가져오기
        challengeId = intent.getLongExtra("challengeId", -1L)
        if (challengeId == -1L) {
            finish()
            return
        }

        val calendarView = binding.CalendarRecyclerView
        val toggleCalendarView = binding.toggleViewButton

        toggleCalendarView.isChecked = true // 주간 보기 초기 상태
        setupCalendar(calendarView, toggleCalendarView)

        // 챌린지 정보 가져오기
        fetchChallengeData()

        // 버튼 클릭 리스너 설정
        binding.preButton1.setOnClickListener {
            goPre()
        }

        // 각 버튼 클릭 시 해당 Fragment로 이동
        binding.myRecord.setOnClickListener {
            showFragment(FragmentOXMyRecord()) // 나의 기록 Fragment
        }

        binding.teamRecord.setOnClickListener {
            showFragment(FragmentOXTeamRecord()) // 팀원 기록 Fragment
        }

        binding.teamRank.setOnClickListener {
            showFragment(OXTeamRank()) // 팀원 순위 Fragment
        }
    }

    private fun setupCalendar(calendarView: MaterialCalendarView, toggleCalendarView: ToggleButton) {
        val selectedDayDecorator = SelectedDayDecorator()

        calendarView.state().edit()
            .setFirstDayOfWeek(Calendar.SUNDAY)
            .setCalendarDisplayMode(CalendarMode.WEEKS)
            .commit()

        calendarView.setOnDateChangedListener { _, date, selected ->
            if (selected) {
                selectedDayDecorator.updateDate(date)
                calendarView.invalidateDecorators() // 데코레이터 새로 고침
            }
        }

        calendarView.addDecorators(
            selectedDayDecorator,
            TodayDecorator(),
            SundayDecorator(),
            SaturdayDecorator()
        )

        toggleCalendarView.setOnCheckedChangeListener { _, isChecked ->
            calendarView.state().edit()
                .setCalendarDisplayMode(if (isChecked) CalendarMode.WEEKS else CalendarMode.MONTHS)
                .commit()
            calendarView.invalidateDecorators()
        }
    }

    private fun goPre() {
        finish()
    }

    private fun showFragment(fragment: Fragment) {
        // 챌린지 ID가 null이 아니어야 전달이 가능
        val bundle = Bundle().apply {
            putLong("challengeId", challengeId ?: -1L)

            // 선택된 날짜를 String으로 변환하여 저장
            selectedDate?.let {
                val selectedDateString = "${it.year}-${it.month}-${it.day}"
                putString("selectedDate", selectedDateString)
            }
        }
        fragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.ox_record_page_nav, fragment)
            .addToBackStack(null) // 백스택에 추가
            .commit()
        supportFragmentManager.executePendingTransactions()
    }

    private fun fetchChallengeData() {
        val accessToken = getAccessToken(this) ?: run {
            Log.w(TAG, "Access Token이 null입니다.")
            showToast("Access Token이 없습니다. 로그인을 다시 시도하세요.")
            return
        }

        val apiService = RetrofitInstance.getInstance().create(ChallengeJoinResultApi::class.java)
        apiService.getChallengeDetails("Bearer $accessToken", challengeId!!)
            .enqueue(object : Callback<ChallengePreviewResponse> {
                override fun onResponse(call: Call<ChallengePreviewResponse>, response: Response<ChallengePreviewResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            binding.challengeDDay.text = "D-${it.result.dday}"
                        }
                    } else {
                    }
                }

                override fun onFailure(call: Call<ChallengePreviewResponse>, t: Throwable) {
                    showToast("네트워크 에러: ${t.localizedMessage}")
                }
            })
    }

    private fun getAccessToken(context: Context): String? {
        val sharedPref = context.getSharedPreferences("saved_user_info", Context.MODE_PRIVATE)
        return sharedPref.getString("accessToken", null)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private inner class SelectedDayDecorator : DayViewDecorator {
        private var selectedDate: CalendarDay? = null
        private val drawable = ContextCompat.getDrawable(this@ChallengeMyNoSmokePageActivity, R.drawable.transparent_calendar_element)

        fun updateDate(date: CalendarDay) {
            selectedDate = date
        }

        override fun shouldDecorate(day: CalendarDay): Boolean {
            return day == selectedDate
        }

        override fun decorate(view: DayViewFacade) {
            drawable?.let { view.setBackgroundDrawable(it) }
            view.addSpan(ForegroundColorSpan(Color.BLACK))
        }
    }

    private inner class TodayDecorator : DayViewDecorator {
        private val drawable = ContextCompat.getDrawable(this@ChallengeMyNoSmokePageActivity, R.drawable.calendar_circle_white)

        override fun shouldDecorate(day: CalendarDay): Boolean {
            return day == CalendarDay.today()
        }

        override fun decorate(view: DayViewFacade) {
            drawable?.let { view.setBackgroundDrawable(it) }
        }
    }

    private class SundayDecorator : DayViewDecorator {
        override fun shouldDecorate(day: CalendarDay): Boolean {
            return day.calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
        }

        override fun decorate(view: DayViewFacade) {
            view.addSpan(ForegroundColorSpan(Color.RED))
        }
    }

    private class SaturdayDecorator : DayViewDecorator {
        override fun shouldDecorate(day: CalendarDay): Boolean {
            return day.calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
        }

        override fun decorate(view: DayViewFacade) {
            view.addSpan(ForegroundColorSpan(Color.BLUE))
        }
    }
}
