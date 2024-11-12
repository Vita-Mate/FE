package com.my.vitamateapp.totalnutrients

import android.graphics.Color
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
import com.my.vitamateapp.mySupplement.NutrientInfo

class SupplementBarAdapter(private var supplementList: List<NutrientInfo>) :
    RecyclerView.Adapter<SupplementBarAdapter.SupplementViewHolder>() {

    private val nutrientColors = listOf(
        Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.CYAN,
        Color.MAGENTA, Color.LTGRAY, Color.DKGRAY, Color.BLACK, Color.GRAY
    )

    // Position 기반으로 색상을 할당하는 함수
    private fun getColorForNutrient(position: Int): Int {
        return nutrientColors[position % nutrientColors.size] // `Int` 반환
    }

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

        holder.nameTextView.text = supplement.nutrientName
        holder.amountTextView.text = String.format(
            holder.itemView.context.getString(R.string.nutrient_amount_format),
            supplement.nutrientAmount.toInt(),
            supplement.recommendedAmount.toInt(),
            supplement.unit
        )

        // 퍼센트 계산 (0에서 100 사이 값으로 조정)
        val percentage = (supplement.nutrientAmount.toFloat() / supplement.recommendedAmount.toFloat()) * 100

        // BarEntry 설정 (X값은 1로 설정하여 막대가 전체 길이로 표시되도록 함)
        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(1f, percentage)) // X값을 1로 설정

        // 데이터셋 생성 및 색상 설정
        val dataSet = BarDataSet(entries, supplement.nutrientName)
        dataSet.color = getColorForNutrient(position) // 색상 설정
        dataSet.setDrawValues(true) // 값 표시 설정

        // BarData 생성 및 막대 너비 설정
        val barData = BarData(dataSet)
        barData.barWidth = 0.7f // 막대 너비 조정

        // 차트에 데이터 설정 및 축 조정
        holder.barChart.data = barData
        holder.barChart.setFitBars(true)

        // X축 및 Y축 설정
        holder.barChart.axisLeft.axisMinimum = 0f
        holder.barChart.axisLeft.axisMaximum = 100f // 0 ~ 100% 범위 설정
        holder.barChart.axisLeft.setDrawLabels(false)
        holder.barChart.axisLeft.setDrawGridLines(false)

        holder.barChart.axisRight.isEnabled = false
        holder.barChart.xAxis.isEnabled = false

        // 불필요한 레이블 숨기기
        holder.barChart.legend.isEnabled = false
        holder.barChart.description.isEnabled = false

        // 애니메이션 적용
        holder.barChart.invalidate()
        holder.barChart.animateY(1000) // 애니메이션

        // 추가적인 문제 방지
        holder.barChart.notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return supplementList.size
    }

    // 데이터 목록을 업데이트하는 함수
    fun updateList(newList: List<NutrientInfo>) {
        supplementList = newList
        notifyDataSetChanged()
    }
}
