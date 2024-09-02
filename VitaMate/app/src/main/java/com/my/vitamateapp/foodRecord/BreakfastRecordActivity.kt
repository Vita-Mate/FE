package com.my.vitamateapp.foodRecord

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.my.vitamateapp.R

class BreakfastRecordActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecordedFoodAdapter
    private val foodItems = mutableListOf<FoodItemModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_breakfast_record)

        recyclerView = findViewById(R.id.recorded_food_rv)

        adapter = RecordedFoodAdapter(foodItems) {
            // + 버튼 클릭 시 동작 정의
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        addSampleData() // 예시 데이터 추가
    }

    private fun addSampleData() {
        // 데이터 추가
        //foodItems.add(FoodItemModel("사과", 14.0, 0.3, 0.2, 52, 1))
        //foodItems.add(FoodItemModel("계란", 1.1, 6.3, 5.0, 68, 2))

        // 데이터 추가 후 어댑터에 알림
        adapter.notifyDataSetChanged()
    }
}




