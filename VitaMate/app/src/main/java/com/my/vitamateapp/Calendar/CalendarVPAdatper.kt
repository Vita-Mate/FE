package com.my.vitamateapp.Calendar

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.my.vitamateapp.Calendar.CalendarOneWeekFragment
import com.my.vitamateapp.Calendar.IDateClickListener
import java.time.LocalDate

class CalendarVPAdatper(
    fragmentActivity: FragmentActivity,
    private val onClickListener: IDateClickListener,
): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun createFragment(position: Int): Fragment {
        return CalendarOneWeekFragment.newInstance(position, onClickListener)
    }
}