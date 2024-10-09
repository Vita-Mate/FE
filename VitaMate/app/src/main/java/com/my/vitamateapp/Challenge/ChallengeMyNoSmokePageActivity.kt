package com.my.vitamateapp.Challenge

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil

import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.ActivityChallengeMyNoSmokePageBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.util.Calendar

class ChallengeMyNoSmokePageActivity : AppCompatActivity() {
    private var selectedDate: CalendarDay? = null
    private lateinit var binding: ActivityChallengeMyNoSmokePageBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 바인딩 초기화
        binding = DataBindingUtil.setContentView(this, R.layout.activity_challenge_my_no_smoke_page)
        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

        // 기존에 findViewById로 참조하던 부분을 binding 객체로 변경
        val calendarView = binding.CalendarRecyclerView
        val toggleCalendarView = binding.toggleViewButton

        toggleCalendarView.isChecked = true // 주간 보기 초기 상태
        setupCalendar(calendarView, toggleCalendarView)

        // 버튼 상태 복원
        restoreButtonState()

        // 클릭 리스너 설정
        binding.preButton1.setOnClickListener {
            goPre()
        }
// 버튼 클릭 리스너 설정
        binding.buttonO.setOnClickListener {
            // O 버튼이 선택되면 X 버튼은 선택 해제
            binding.buttonO.isSelected = true
            binding.buttonX.isSelected = false
            binding.buttonO.background = ContextCompat.getDrawable(this, R.drawable.button_default_background) // O 버튼 배경 설정
            binding.buttonX.background = null // X 버튼 배경 초기화
            saveButtonState(true)
        }

        binding.buttonX.setOnClickListener {
            // X 버튼이 선택되면 O 버튼은 선택 해제
            binding.buttonX.isSelected = true
            binding.buttonO.isSelected = false
            binding.buttonX.background = ContextCompat.getDrawable(this, R.drawable.button_default_background) // X 버튼 배경 설정
            binding.buttonO.background = null // O 버튼 배경 초기화
            saveButtonState(false)
        }
    }

    private fun saveButtonState(isOSelected: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("buttonOState", isOSelected)
        editor.apply()
    }

    private fun restoreButtonState() {
        val isOSelected = sharedPreferences.getBoolean("buttonOState", false)
        binding.buttonO.isSelected = isOSelected
    }

    private fun setupCalendar(calendarView: MaterialCalendarView, toggleCalendarView: ToggleButton) {
        calendarView.state().edit()
            .setFirstDayOfWeek(Calendar.SUNDAY)
            .setCalendarDisplayMode(CalendarMode.WEEKS)
            .commit()

        calendarView.setOnDateChangedListener { widget, date, selected ->
            if (selected) {
                // 기존 선택된 날짜의 데코레이터 제거
                selectedDate?.let {
                    calendarView.removeDecorators()
                    calendarView.addDecorators(TodayDecorator(), SundayDecorator(), SaturdayDecorator()
                    )
                }

                // 새로 선택된 날짜로 데코레이터 추가
                selectedDate = date
                calendarView.addDecorator(SelectedDayDecorator(date))
                calendarView.invalidateDecorators() // 데코레이터 새로 고침
            }
        }

        calendarView.addDecorators(
            TodayDecorator(),
            SundayDecorator(),
            SaturdayDecorator()
        )

        toggleCalendarView.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // 주간 보기
                calendarView.state().edit()
                    .setCalendarDisplayMode(CalendarMode.WEEKS)
                    .commit()
            } else {
                // 월간 보기
                calendarView.state().edit()
                    .setCalendarDisplayMode(CalendarMode.MONTHS)
                    .commit()
            }
            calendarView.invalidateDecorators() // 데코레이터 새로 고침
        }
    }


    private fun goPre() {
        finish()
    }

    private inner class SelectedDayDecorator(private val selectedDate: CalendarDay) : DayViewDecorator {
        private val drawable =
            ContextCompat.getDrawable(this@ChallengeMyNoSmokePageActivity, R.drawable.transparent_calendar_element) // 여기에 원하는 drawable을 설정

        override fun shouldDecorate(day: CalendarDay): Boolean {
            return day == selectedDate
        }

        override fun decorate(view: DayViewFacade) {
            view.addSpan(ForegroundColorSpan(Color.BLACK))
            drawable?.let { view.setBackgroundDrawable(it) }
        }
    }

    // 데코레이터 클래스들 그대로 사용
    private inner class TodayDecorator : DayViewDecorator {
        private val drawable =
            ContextCompat.getDrawable(this@ChallengeMyNoSmokePageActivity, R.drawable.calendar_circle_white)

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
}
