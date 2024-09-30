package com.my.vitamateapp.Challenge

import android.graphics.Color
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.widget.ImageButton
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.my.vitamateapp.Challenge.ChallengeBottomSheetDialogFragment
import com.my.vitamateapp.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.util.Calendar

class ChallengeMypageActivity : AppCompatActivity() {

    private lateinit var calendarView: MaterialCalendarView
    private lateinit var toggleCalendarView: ToggleButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.challenge_exercise_mypage1)

        calendarView = findViewById(R.id.CalendarRecyclerView)
        toggleCalendarView = findViewById(R.id.toggleViewButton)

        toggleCalendarView.isChecked = true // 주간 보기 초기 상태
        setupCalendar()

        val addMyRecordButton: ImageButton = findViewById(R.id.add_my_record)
        addMyRecordButton.setOnClickListener {
            showChallengeBottomSheetDialog()
        }

        toggleCalendarView.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // 월간 보기
                calendarView.state().edit()
                    .setCalendarDisplayMode(CalendarMode.MONTHS)
                    .commit()
            } else {
                // 주간 보기
                calendarView.state().edit()
                    .setCalendarDisplayMode(CalendarMode.WEEKS)
                    .commit()
            }
            calendarView.invalidateDecorators() // 데코레이터 새로 고침
        }
    }

    private fun setupCalendar() {
        calendarView.state().edit()
            .setFirstDayOfWeek(Calendar.SUNDAY)
            .setCalendarDisplayMode(CalendarMode.MONTHS)
            .commit()

        calendarView.setOnDateChangedListener { _, date, _ ->
            // 선택된 날짜에 대한 처리
        }

        calendarView.addDecorators(
            TodayDecorator(),
            SundayDecorator(),
            SaturdayDecorator()
        )
    }

    private fun showChallengeBottomSheetDialog() {
        val bottomSheetFragment = ChallengeBottomSheetDialogFragment()
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }

    private inner class TodayDecorator : DayViewDecorator {
        private val drawable =
            ContextCompat.getDrawable(this@ChallengeMypageActivity, R.drawable.calendar_circle_gray)

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
