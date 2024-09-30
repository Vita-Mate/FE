package com.my.vitamateapp.totalnutrients

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.my.vitamateapp.R

class SupplementBarAdapter(private val supplementList: List<SupplementBarModel>) :
    RecyclerView.Adapter<SupplementBarAdapter.SupplementViewHolder>() {

    inner class SupplementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.supplementName)
        val amountTextView: TextView = itemView.findViewById(R.id.supplementAmount)
        val barChart: HorizontalBarChart = itemView.findViewById(R.id.barChart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupplementViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.supplement_bar_item, parent, false)
        return SupplementViewHolder(view)
    }

    override fun onBindViewHolder(holder: SupplementViewHolder, position: Int) {
        val supplement = supplementList[position]

        // 영양제 이름 설정
        holder.nameTextView.text = supplement.name

        // 섭취량/목표량 설정
        holder.amountTextView.text = "${supplement.currentAmount.toInt()}/${supplement.targetAmount.toInt()}"

        // 막대그래프 설정
        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(0f, supplement.currentAmount))

        val dataSet = BarDataSet(entries, supplement.name)
        dataSet.color = supplement.color // 영양제별 색상 설정

        val barData = BarData(dataSet)
        holder.barChart.data = barData

        // 그래프 스타일 설정
        holder.barChart.setFitBars(true)
        holder.barChart.description.isEnabled = false
        holder.barChart.legend.isEnabled = false
        holder.barChart.xAxis.setDrawLabels(false)
        holder.barChart.axisLeft.setDrawLabels(false)
        holder.barChart.axisRight.setDrawLabels(false)

        // 그래프 갱신
        holder.barChart.invalidate()
    }

    override fun getItemCount(): Int {
        return supplementList.size
    }
}
