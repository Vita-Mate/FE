import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.my.vitamateapp.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Calendar

class CalendarMainActivity : AppCompatActivity() {

    private lateinit var calendarView: MaterialCalendarView
    private lateinit var toggleButton: ToggleButton

    // Decorators
    private lateinit var dayDecorator: DayDecorator
    private lateinit var todayDecorator: TodayDecorator
    private lateinit var sundayDecorator: SundayDecorator
    private lateinit var saturdayDecorator: SaturdayDecorator
    private lateinit var selectedMonthDecorator: SelectedMonthDecorator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.challenge_exercise_mypage1)

        calendarView = findViewById(R.id.CalendarRecyclerView)
        toggleButton = findViewById(R.id.toggleViewButton)

        // 주의 이름을 한글로 설정
        calendarView.setWeekDayFormatter(ArrayWeekDayFormatter(resources.getTextArray(R.array.custom_weekdays)))

        // Decorators 초기화
        dayDecorator = DayDecorator(this)
        todayDecorator = TodayDecorator(this)
        sundayDecorator = SundayDecorator()
        saturdayDecorator = SaturdayDecorator()
        selectedMonthDecorator = SelectedMonthDecorator(CalendarDay.today().month)

        // 캘린더에 Decorators 추가
        updateDecorators()

        // 날짜 선택 리스너 설정
        calendarView.setOnDateChangedListener { _, date, _ ->
            // `month`는 0부터 시작하므로 +1 필요
            val formattedDate = "${date.year}-${date.month + 1}-${date.day}"
            Log.d("CalendarMainActivity", "Selected date saved: $formattedDate")
        }


        // 초기 설정은 월간 모드
        setupMonthView()

        // ToggleButton을 사용하여 주간/월간 모드 전환
        toggleButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setupMonthView()  // 주간 모드로 전환
            } else {
                setupWeekView()  // 월간 모드로 전환
            }
            Log.d("CalendarMainActivity", "Switch calendar mode: ${if (isChecked) "Weekly" else "Monthly"}")
        }

        // 캘린더에 보여지는 Month가 변경된 경우
        calendarView.setOnMonthChangedListener { _, date ->
            selectedMonthDecorator = SelectedMonthDecorator(date.month)
            updateDecorators()
            Log.d("CalendarMainActivity", "Month changed: ${date.year}-${date.month + 1}")
        }
    }

    private fun updateDecorators() {
        calendarView.removeDecorators()
        calendarView.addDecorators(
            dayDecorator,
            todayDecorator,
            sundayDecorator,
            saturdayDecorator,
            selectedMonthDecorator
        )
        calendarView.invalidateDecorators() // Decorators를 갱신합니다.
    }

    // 월간 모드 설정
    private fun setupMonthView() {
        calendarView.state().edit()
            .setFirstDayOfWeek(Calendar.SUNDAY)
            .setCalendarDisplayMode(CalendarMode.MONTHS)
            .commit()
    }

    // 주간 모드 설정
    private fun setupWeekView() {
        calendarView.state().edit()
            .setFirstDayOfWeek(Calendar.SUNDAY)
            .setCalendarDisplayMode(CalendarMode.WEEKS)
            .commit()
    }

    /* 선택된 날짜의 background를 설정하는 클래스 */
    private inner class DayDecorator(context: Context) : DayViewDecorator {
        private val drawable = ContextCompat.getDrawable(context, R.drawable.calendar_selector)

        override fun shouldDecorate(day: CalendarDay): Boolean = true

        override fun decorate(view: DayViewFacade) {
            drawable?.let { view.setSelectionDrawable(it) }
        }
    }

    /* 오늘 날짜의 background를 설정하는 클래스 */
    private class TodayDecorator(context: Context) : DayViewDecorator {
        private val drawable = ContextCompat.getDrawable(context, R.drawable.calendar_circle_white)
        private val today = CalendarDay.today()

        override fun shouldDecorate(day: CalendarDay?): Boolean {
            return day?.equals(today) ?: false
        }

        override fun decorate(view: DayViewFacade) {
            view.setBackgroundDrawable(drawable!!)
        }
    }

    /* 이번달에 속하지 않지만 캘린더에 보여지는 이전달/다음달의 일부 날짜를 설정하는 클래스 */
    private inner class SelectedMonthDecorator(val selectedMonth: Int) : DayViewDecorator {
        override fun shouldDecorate(day: CalendarDay): Boolean {
            return day.month != selectedMonth
        }

        override fun decorate(view: DayViewFacade) {
            view.addSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        this@CalendarMainActivity,
                        R.color.black
                    )
                )
            )
        }
    }

    /* 일요일 날짜의 색상을 설정하는 클래스 */
    private class SundayDecorator : DayViewDecorator {
        override fun shouldDecorate(day: CalendarDay): Boolean {
            val localDate = LocalDate.of(day.year, day.month + 1, day.day)
            return localDate.dayOfWeek == DayOfWeek.SUNDAY
        }

        override fun decorate(view: DayViewFacade) {
            view.addSpan(ForegroundColorSpan(Color.RED))
        }
    }

    /* 토요일 날짜의 색상을 설정하는 클래스 */
    private class SaturdayDecorator : DayViewDecorator {
        override fun shouldDecorate(day: CalendarDay): Boolean {
            val localDate = LocalDate.of(day.year, day.month + 1, day.day)
            return localDate.dayOfWeek == DayOfWeek.SATURDAY
        }

        override fun decorate(view: DayViewFacade) {
            view.addSpan(ForegroundColorSpan(Color.BLUE))
        }
    }
}