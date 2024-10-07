package com.my.vitamateapp.Challenge

import android.graphics.Color
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.ChallengeExerciseMypage1Binding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.util.Calendar

class ChallengeMyExercisePageActivity : AppCompatActivity() {

    private lateinit var binding: ChallengeExerciseMypage1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 바인딩 초기화
        binding = DataBindingUtil.setContentView(this, R.layout.challenge_exercise_mypage1)

        // 기존에 findViewById로 참조하던 부분을 binding 객체로 변경
        val calendarView = binding.CalendarRecyclerView
        val toggleCalendarView = binding.toggleViewButton

        toggleCalendarView.isChecked = true // 주간 보기 초기 상태
        setupCalendar(calendarView, toggleCalendarView)

        // 클릭 리스너 설정
        binding.preButton1.setOnClickListener {
            goPre()
        }

        binding.addMyRecord.setOnClickListener {
            showChallengeBottomSheetDialog()
        }
    }

    private fun setupCalendar(calendarView: MaterialCalendarView, toggleCalendarView: ToggleButton) {
        calendarView.state().edit()
            .setFirstDayOfWeek(Calendar.SUNDAY)
            .setCalendarDisplayMode(CalendarMode.WEEKS)
            .commit()

        calendarView.setOnDateChangedListener { _, date, _ ->
            // 선택된 날짜에 대한 처리
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

    private fun showChallengeBottomSheetDialog() {
        val bottomSheetFragment = ChallengeBottomSheetDialogFragment()
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }

    private fun goPre() {
        finish()
    }

    // 데코레이터 클래스들 그대로 사용
    private inner class TodayDecorator : DayViewDecorator {
        private val drawable =
            ContextCompat.getDrawable(this@ChallengeMyExercisePageActivity, R.drawable.calendar_circle_gray)

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
