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
import com.my.vitamateapp.Challenge.ChallengeBottomSheetDialogFragment
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

        challengeId = intent.getLongExtra("challengeId", -1L)  // 변경된 부분
        if (challengeId == -1L) {
            Log.e("ChallengeMyExercisePageActivity", "Invalid Challenge ID received")
            showToast("챌린지 ID가 유효하지 않습니다.")
            finish()
        }



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
            showChallengeBottomSheetDialog() // 수정된 부분
        }
    }

    private fun setupCalendar(calendarView: MaterialCalendarView, toggleCalendarView: ToggleButton) {
        calendarView.state().edit()
            .setFirstDayOfWeek(Calendar.SUNDAY)
            .setCalendarDisplayMode(CalendarMode.WEEKS)
            .commit()

        calendarView.setOnDateChangedListener { widget, date, selected ->
            if (selected) {
                selectedDate?.let {
                    calendarView.removeDecorators()
                    calendarView.addDecorators(TodayDecorator(), SundayDecorator(), SaturdayDecorator())
                }

                selectedDate = date
                calendarView.addDecorator(SelectedDayDecorator(date))
                calendarView.invalidateDecorators()
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

    private fun showChallengeBottomSheetDialog() {
        challengeId?.let {
            val bottomSheetFragment = ChallengeBottomSheetDialogFragment.new(it.toString())
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        } ?: run {
            Toast.makeText(this, "챌린지 ID가 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }


    private fun goPre() {
        finish()
    }

    private inner class SelectedDayDecorator(private val selectedDate: CalendarDay) : DayViewDecorator {
        private val drawable = ContextCompat.getDrawable(this@ChallengeMyExercisePageActivity, R.drawable.transparent_calendar_element)

        override fun shouldDecorate(day: CalendarDay): Boolean {
            return day == selectedDate
        }

        override fun decorate(view: DayViewFacade) {
            view.addSpan(ForegroundColorSpan(Color.BLACK))
            drawable?.let { view.setBackgroundDrawable(it) }
        }
    }

    private inner class TodayDecorator : DayViewDecorator {
        private val drawable = ContextCompat.getDrawable(this@ChallengeMyExercisePageActivity, R.drawable.calendar_circle_white)

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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}