package com.my.vitamateapp.Challenge

import android.graphics.Color
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.datepicker.MaterialCalendar
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.ChallengeExerciseMypage1Binding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.util.Calendar

class ChallengeMyExercisePageActivity : AppCompatActivity() {
    private var selectedDate: CalendarDay? = null
    private var challengeId: Long? = null

    private lateinit var binding: ChallengeExerciseMypage1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 바인딩 초기화
        binding = DataBindingUtil.setContentView(this, R.layout.challenge_exercise_mypage1)

        // 캘린더 참조
        val calendarView = binding.CalendarRecyclerView // 여기서 calendarView를 바인딩 객체에서 가져옴


        challengeId = intent.getLongExtra("challengeId", -1L)  // challengeId를 Intent에서 받음
        Log.d("ChallengeMyExercisePageActivity", "Received challengeId: $challengeId")



        // 캘린더에서 날짜 선택 리스너 설정
        calendarView.setOnDateChangedListener { widget, date, selected ->
            if (selected) {
                selectedDate = date  // 선택된 날짜를 CalendarDay 객체로 저장
                calendarView.addDecorator(SelectedDayDecorator(date))  // 날짜를 선택된 상태로 표시
                calendarView.invalidateDecorators()

                // 선택된 날짜를 String 형식으로 변환하여 Fragment에 전달
                val selectedDateString = "${date.year}-${date.month + 1}-${date.day}"

                // Fragment에 전달할 데이터 준비
                val bundle = Bundle().apply {
                    putLong("challengeId", challengeId ?: -1L)
                    putString("selectedDate", selectedDateString)
                }

                val fragment = FragmentMyExerciseRecord()  // 전달할 Fragment
                fragment.arguments = bundle

                // Fragment를 화면에 추가
                supportFragmentManager.beginTransaction()
                    .replace(R.id.record_page_nav, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }


        // 캘린더 토글 버튼 설정
        val toggleCalendarView = binding.toggleViewButton
        toggleCalendarView.isChecked = false // 기본은 주간 보기
        setupCalendar(calendarView, toggleCalendarView)

        // 클릭 리스너 설정
        binding.preButton1.setOnClickListener {
            goPre()
        }

        // 각 버튼 클릭 시 해당 Fragment로 이동
        binding.myRecord.setOnClickListener {
            showFragment(FragmentMyExerciseRecord()) // 나의 기록 Fragment
        }

        binding.teamRecord.setOnClickListener {
            showFragment(FragmentTeamExerciseRecord()) // 팀원 기록 Fragment
        }

        binding.teamRank.setOnClickListener {
            showFragment(TeamRank()) // 팀원 순위 Fragment
        }
    }

    private fun setupCalendar(calendarView: MaterialCalendarView, toggleCalendarView: ToggleButton) {
        calendarView.setOnDateChangedListener { widget, date, selected ->
            if (selected) {
                selectedDate?.let {
                    calendarView.removeDecorators()
                    calendarView.addDecorators(TodayDecorator(),
                        SundayDecorator(),
                        SaturdayDecorator()
                    )
                }

                selectedDate = date
                calendarView.addDecorator(SelectedDayDecorator(date))
                calendarView.invalidateDecorators()

                // 선택된 날짜를 SharedPreferences에 저장
                saveSelectedDate(date)
            }
        }


        calendarView.addDecorators(
            TodayDecorator(),
            SundayDecorator(),
            SaturdayDecorator()
        )

        toggleCalendarView.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                calendarView.state().edit()
                    .setCalendarDisplayMode(CalendarMode.WEEKS)
                    .commit()
            } else {
                calendarView.state().edit()
                    .setCalendarDisplayMode(CalendarMode.MONTHS)
                    .commit()
            }
            calendarView.invalidateDecorators()
        }
    }

    private fun goPre() {
        finish()
    }

    private fun showFragment(fragment: androidx.fragment.app.Fragment) {
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
            .replace(R.id.record_page_nav, fragment)
            .addToBackStack(null) // 백스택에 추가
            .commit()
        supportFragmentManager.executePendingTransactions()
    }





    private inner class SelectedDayDecorator(private val selectedDate: CalendarDay) :
        DayViewDecorator {
        private val drawable = ContextCompat.getDrawable(
            this@ChallengeMyExercisePageActivity,
            R.drawable.transparent_calendar_element
        )

        override fun shouldDecorate(day: CalendarDay): Boolean {
            return day == selectedDate
        }

        override fun decorate(view: DayViewFacade) {
            view.addSpan(ForegroundColorSpan(Color.BLACK))
            drawable?.let { view.setBackgroundDrawable(it) }
        }
    }

    private inner class TodayDecorator : DayViewDecorator {
        private val drawable = ContextCompat.getDrawable(
            this@ChallengeMyExercisePageActivity,
            R.drawable.calendar_circle_white
        )

        override fun shouldDecorate(day: CalendarDay): Boolean {
            return day == CalendarDay.today()
        }

        override fun decorate(view: DayViewFacade) {
            drawable?.let { view.setBackgroundDrawable(it) }
        }
    }

    private class SundayDecorator : DayViewDecorator {
        override fun shouldDecorate(day: CalendarDay): Boolean {
            val currentCalendar = Calendar.getInstance()
            return day.calendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH) &&
                    day.calendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) &&
                    day.calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
        }

        override fun decorate(view: DayViewFacade) {
            view.addSpan(ForegroundColorSpan(Color.RED))
        }
    }

    private class SaturdayDecorator : DayViewDecorator {
        override fun shouldDecorate(day: CalendarDay): Boolean {
            val currentCalendar = Calendar.getInstance()
            return day.calendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH) &&
                    day.calendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) &&
                    day.calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
        }

        override fun decorate(view: DayViewFacade) {
            view.addSpan(ForegroundColorSpan(Color.BLUE))
        }
    }

    private fun saveSelectedDate(date: CalendarDay) {
        val sharedPref = getSharedPreferences("selected_date", MODE_PRIVATE)
        val editor = sharedPref.edit()

        // 선택된 날짜를 String 형태로 저장 (예: "yyyy-MM-dd")
        val selectedDateString = "${date.year}-${date.month}-${date.day}"
        editor.putString("selected_date", selectedDateString)
        editor.apply()

        Log.d("ChallengeMyExercisePage", "Selected date saved: $selectedDateString")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}