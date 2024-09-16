package com.my.vitamateapp.Calendar

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil.setContentView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.CalendarMainBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

class CalendarMainActivity : AppCompatActivity() {

private lateinit var calendarRecyclerView: RecyclerView

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.challenge_exercise_mypage1)

    calendarRecyclerView = findViewById(R.id.CalendarRecyclerView)


    // 월간 뷰 (7x5 그리드 레이아웃)
    calendarRecyclerView.layoutManager = GridLayoutManager(this, 7)

    // 주간 뷰로 표시하고 싶다면 LinearLayoutManager를 사용할 수 있습니다.
    // calendarRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

    val calendarDays = generateMonthlyCalendar(2024, 9) // 예: 2024년 9월의 달력 생성
    val adapter = CalendarAdapter(calendarDays)
    calendarRecyclerView.adapter = adapter
}

// 월간 달력 생성
private fun generateMonthlyCalendar(year: Int, month: Int): List<CalendarDay> {
    val calendar = Calendar.getInstance()
    calendar.set(year, month - 1, 1)

    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    val dayList = mutableListOf<CalendarDay>()

    for (day in 1..daysInMonth) {
        dayList.add(CalendarDay(day, month, year))

    }
// dayList에 값이 잘 추가되었는지 확인하는 로그
    Log.d("Calendar", "Generated days: $dayList")

    return dayList
}

// 주간 달력 생성
private fun generateWeeklyCalendar(startDate: Calendar): List<CalendarDay> {
    val dayList = mutableListOf<CalendarDay>()

    for (i in 0..6) { // 한 주는 7일
        val day = startDate.get(Calendar.DAY_OF_MONTH)
        val month = startDate.get(Calendar.MONTH) + 1 // Calendar.MONTH는 0부터 시작
        val year = startDate.get(Calendar.YEAR)

        dayList.add(CalendarDay(day, month, year))
        startDate.add(Calendar.DAY_OF_MONTH, 1) // 하루씩 추가
    }
    // dayList에 값이 잘 추가되었는지 확인하는 로그
    Log.d("Calendar", "Generated days: $dayList")


    return dayList
}
}