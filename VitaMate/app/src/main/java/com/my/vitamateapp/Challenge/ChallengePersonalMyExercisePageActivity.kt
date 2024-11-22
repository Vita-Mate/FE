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
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.ChallengeExerciseMypage1Binding
import com.my.vitamateapp.databinding.ChallengePersonalExerciseMypage1Binding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.util.Calendar

class ChallengePersonalMyExercisePageActivity : AppCompatActivity() {
    private var selectedDate: CalendarDay? = null
    private var challengeId: Long? = null

    private lateinit var binding: ChallengePersonalExerciseMypage1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 바인딩 초기화
        binding = DataBindingUtil.setContentView(this, R.layout.challenge_personal_exercise_mypage1)

        challengeId = intent.getLongExtra("challengeId", -1L)  // challengeId를 Intent에서 받음
        Log.d("ChallengeMyExercisePageActivity", "Received challengeId: $challengeId")


        // 캘린더 설정
        val calendarView = binding.CalendarRecyclerView
        calendarView.state().edit()
            .setFirstDayOfWeek(Calendar.SUNDAY)
            .setCalendarDisplayMode(CalendarMode.WEEKS)
            .commit()
        calendarView.setCurrentDate(CalendarDay.today()) // 오늘 날짜로 초기화

        // 캘린더 토글 버튼 설정
        val toggleCalendarView = binding.toggleViewButton
        toggleCalendarView.isChecked = true // 기본은 주간 보기
        setupCalendar(calendarView, toggleCalendarView)

        // 클릭 리스너 설정
        binding.preButton1.setOnClickListener {
            goPre()
        }

        // 각 버튼 클릭 시 해당 Fragment로 이동
        binding.myRecord.setOnClickListener {
            showFragment(FragmentMyExerciseRecord()) // 나의 기록 Fragment
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

    }





    private inner class SelectedDayDecorator(private val selectedDate: CalendarDay) :
        DayViewDecorator {
        private val drawable = ContextCompat.getDrawable(
            this@ChallengePersonalMyExercisePageActivity,
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
            this@ChallengePersonalMyExercisePageActivity,
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

        Log.d("ChallengePersonalMyExercisePage", "Selected date saved: $selectedDateString")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}