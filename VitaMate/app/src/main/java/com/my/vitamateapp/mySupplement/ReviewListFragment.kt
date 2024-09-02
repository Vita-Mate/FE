package com.my.vitamateapp.mySupplement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.my.vitamateapp.R

class ReviewListFragment : Fragment() {

    private lateinit var rvAdatper : ReviewListAdapter
    private val reviews = mutableListOf(
        ReviewItemModel("냥냥냥", 4.0f, "먹고 건강해졌습니다."),
        ReviewItemModel("홍길동", 5.0f, "정말 좋아요!"),
        ReviewItemModel("철수", 3.5f, "괜찮지만 더 지켜봐야겠어요.")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_review_list, container, false)
        val showMoreButton: Button = view.findViewById(R.id.show_review_button)

        val rv : RecyclerView = view.findViewById(R.id.review_item_rv)
        rvAdatper = ReviewListAdapter(reviews)
        rv.adapter = rvAdatper
        rv.layoutManager = LinearLayoutManager(context)

        showMoreButton.setOnClickListener {
            rvAdatper.expandItems() // 전체 아이템 보기
            showMoreButton.visibility = View.GONE // 버튼 숨기기
        }

        return view
    }
}