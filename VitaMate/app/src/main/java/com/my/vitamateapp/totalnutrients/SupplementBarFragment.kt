package com.my.vitamateapp.totalnutrients

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.my.vitamateapp.R

class SupplementBarFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        val view = inflater.inflate(R.layout.fragment_supplement_bar, container, false)

        // RecyclerView 초기화
        val recyclerView = view.findViewById<RecyclerView>(R.id.supplement_bar_rv)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // 예시 영양제 리스트
        val supplements = listOf(
            SupplementBarModel("비타민 A", 6f, 12f, Color.parseColor("#FFA500")),
            SupplementBarModel("비타민 D", 3f, 18f, Color.parseColor("#76C7C0")),
            SupplementBarModel("루테인", 5f, 28f, Color.parseColor("#87CEEB")),
            SupplementBarModel("오메가 3", 2f, 15f, Color.parseColor("#FFD700"))
        )

        val adapter = SupplementBarAdapter(supplements)
        recyclerView.adapter = adapter

        return view // 인플레이트한 View 반환
    }
}
