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
        Color.parseColor("#FF8094"),  // 진한 파스텔 핑크
        Color.parseColor("#FFB88C"),  // 진한 파스텔 오렌지
        Color.parseColor("#FFEC7A"),  // 진한 파스텔 노랑
        Color.parseColor("#91FF99"),  // 진한 파스텔 민트
        Color.parseColor("#90BFFF"),  // 진한 파스텔 블루
        Color.parseColor("#A98CFF"),  // 진한 파스텔 라벤더
        Color.parseColor("#88E0A0"),  // 진한 파스텔 그린
        Color.parseColor("#A0FFEB"),  // 진한 파스텔 청록색
        Color.parseColor("#FFB8A0")   // 진한 파스텔 코랄
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

        // 권장 섭취량보다 실제 섭취량이 많을 경우 텍스트를 빨간색과 Bold로 설정
        if (supplement.nutrientAmount > supplement.recommendedAmount + 0.2f) {
            holder.amountTextView.setTextColor(Color.parseColor("#FF6F6F")) // 파스텔 핑크 색상 설정
            holder.amountTextView.setTypeface(null, android.graphics.Typeface.BOLD) // Bold 설정
        } else {
            holder.amountTextView.setTextColor(Color.BLACK) // 검은색으로 초기화
            holder.amountTextView.setTypeface(null, android.graphics.Typeface.NORMAL) // 일반체로 초기화
        }

        // 퍼센트 계산 (0에서 100 사이 값으로 조정)
        val percentage = (supplement.nutrientAmount.toFloat() / supplement.recommendedAmount.toFloat()) * 100

        // BarEntry 설정 (X값은 1로 설정하여 막대가 전체 길이로 표시되도록 함)
        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(1f, percentage)) // X값을 1로 설정

        // 데이터셋 생성 및 색상 설정
        val dataSet = BarDataSet(entries, supplement.nutrientName)
        dataSet.color = getColorForNutrient(position) // 색상 설정
        dataSet.setDrawValues(false) // 숫자 숨기기

        // BarData 생성 및 막대 너비 설정
        val barData = BarData(dataSet)
        barData.barWidth = 0.5f // 막대 너비 조정 (기본값보다 크게 설정)

        // 차트에 데이터 설정 및 축 조정
        holder.barChart.data = barData
        holder.barChart.setFitBars(true)

        // X축 및 Y축 설정
        holder.barChart.axisLeft.axisMinimum = 0f
        holder.barChart.axisLeft.axisMaximum = 100f // 0 ~ 100% 범위 설정
        holder.barChart.axisLeft.setDrawLabels(false)
        holder.barChart.axisLeft.setDrawGridLines(false)
        holder.barChart.axisLeft.setDrawAxisLine(false)

        holder.barChart.axisRight.isEnabled = false
        holder.barChart.xAxis.isEnabled = false

        // 불필요한 레이블 숨기기
        holder.barChart.legend.isEnabled = false
        holder.barChart.description.isEnabled = false

        // 테두리 추가: XML에서 background 속성 사용
        holder.barChart.setDrawBarShadow(true) // 막대의 배경 그림자 활성화

        // 하이라이트 비활성화
        holder.barChart.isHighlightPerTapEnabled = false // 터치 시 하이라이트 비활성화
        holder.barChart.isHighlightPerDragEnabled = false // 드래그 시 하이라이트 비활성화

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