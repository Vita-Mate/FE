package com.my.vitamateapp.foodRecord

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.my.vitamateapp.databinding.ActivityRecordBreakfastBinding
import com.my.vitamateapp.mySupplement.SupplementDetailActivity

class BreakfastRecordActivity : AppCompatActivity() {

    private lateinit var adapter: RecordedFoodAdapter
    private val foodItems = mutableListOf<FoodItemModel>()
    private lateinit var binding: ActivityRecordBreakfastBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding 초기화
        binding = ActivityRecordBreakfastBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // RecyclerView 초기화
        binding.recordedFoodRv.layoutManager = LinearLayoutManager(this)
        adapter = RecordedFoodAdapter(foodItems) {
            //음식 검색 액티비티로 전환
            val intent = Intent(this, SearchFoodActivity::class.java)
            startActivity(intent)
        }
        binding.recordedFoodRv.adapter = adapter

        addSampleData() // 예시 데이터 추가

        // < 버튼 클릭 시 이전 페이지로 이동
        binding.preButton.setOnClickListener {
            goPre()
        }
    }

    private fun addSampleData() {
        // 예시 데이터 추가
        foodItems.add(FoodItemModel("사과", 14.0, 0.3, 0.2, 52, 1))
        foodItems.add(FoodItemModel("계란", 1.1, 6.3, 5.0, 68, 2))

        // 데이터 추가 후 어댑터에 알림
        adapter.notifyDataSetChanged()
    }

    private fun goPre() {
        finish() // 이전 페이지로 이동
    }
}



