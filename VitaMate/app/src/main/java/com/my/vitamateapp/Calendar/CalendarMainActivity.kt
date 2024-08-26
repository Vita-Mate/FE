package com.my.vitamateapp.Calendar

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.my.vitamateapp.Calendar.IDateClickListener
import com.my.vitamateapp.databinding.CalendarMainBinding
import com.my.vitamateapp.Calendar.CalendarVPAdatper
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity(), IDateClickListener {
    private lateinit var binding: CalendarMainBinding

    var today: LocalDate = LocalDate.now()
    private lateinit var selectedDate: LocalDate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CalendarMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectedDate = today // 최초에는 선택 날짜를 오늘로 초기화

        // 주간 달력 뷰페이저
        setOneWeekViewPager()
    }

    override fun onStart() {
        super.onStart()

        binding.mainTopDateTv.text = dateFormat(selectedDate)
    }

    /** 주간 달력 */
    private fun setOneWeekViewPager() {
        saveSelectedDate(today)
        val calendarAdapter = CalendarVPAdatper(this, this)
        binding.mainWeeklyCalendarDateVp.adapter = calendarAdapter
        binding.mainWeeklyCalendarDateVp.setCurrentItem(Int.MAX_VALUE / 2, false)
    }

    private fun saveSelectedDate(date: LocalDate) {
        val sharedPreference = this.getSharedPreferences("CALENDAR-APP", AppCompatActivity.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = sharedPreference.edit()
        editor.putString("SELECTED-DATE", date.toString())
        editor.apply()
    }

    private fun dateFormat(date: LocalDate): String{
        val formatter = DateTimeFormatter.ofPattern(DATE_PATTERN)
        return date.format(formatter)
    }

    override fun onClickDate(date: LocalDate) {
        selectedDate = date
        // 선택 날짜 저장
        saveSelectedDate(date)
        // 선택한 날짜 표시
        binding.mainTopDateTv.text = dateFormat(date)
        //TODO: API 호출 - 오늘 날짜 목표 받아오기
    }

    companion object {
        const val DATE_PATTERN = "yyyy년 MM월 dd일"
    }
}