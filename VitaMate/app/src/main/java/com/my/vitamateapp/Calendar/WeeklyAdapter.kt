package com.my.vitamateapp.Calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.my.vitamateapp.R

class WeeklyAdapter(private val daysList: List<CalendarDay>) :
    RecyclerView.Adapter<WeeklyAdapter.WeeklyViewHolder>() {

    class WeeklyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayTextView: TextView = itemView.findViewById(R.id.tv_1)  // 주간 레이아웃의 날짜 텍스트뷰
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeeklyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_calendar_one_week, parent, false)
        return WeeklyViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeeklyViewHolder, position: Int) {
        val day = daysList[position]
        holder.dayTextView.text = day.day.toString()
    }

    override fun getItemCount(): Int {
        return daysList.size
    }
}
