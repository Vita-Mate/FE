package com.my.vitamateapp.mySupplement

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.my.vitamateapp.R

class ReviewListAdapter(private val items: MutableList<ReviewItem>, private val isDetailView: Boolean) : RecyclerView.Adapter<ReviewListAdapter.ViewHolder>() {

    // 전체보기 여부
    private var isExpanded = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutRes = if (viewType == 0) {
            // 영양제 디테일 액티비티용 레이아웃
            R.layout.review_item
        } else {
            // 전체 리뷰 보기 액티비티용 레이아웃
            R.layout.review_all_item
        }
        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // 아이템이 비어있지 않으면 아이템을 바인딩하고, 비어 있으면 "리뷰가 없습니다" 메시지를 바인딩
        if (items.isEmpty()) {
            holder.bindNoReviews()
        } else {
            // 아이템의 위치가 유효한지 확인
            if (position >= 0 && position < items.size) {
                holder.bindItems(items[position])
            } else {
                Log.w("ReviewListAdapter", "Invalid position: $position")
            }
        }
    }


    override fun getItemCount(): Int {
        Log.d("ReviewListAdapter", "isDetailView: $isDetailView, Item count: ${if (isDetailView) minOf(items.size, 2) else items.size}")
        // isDetailView가 true일 때 2개만 표시하고, false일 때는 전체 표시
        return if (isDetailView) minOf(items.size, 2) else items.size
    }

    override fun getItemViewType(position: Int): Int {
        // isDetailView가 true면 '영양제 디테일' 레이아웃을 사용하고, false일 때는 '전체 리뷰' 레이아웃을 사용
        return if (isDetailView) 0 else 1
    }


    // 전체 아이템 보여주기 함수
    fun expandItems() {
        isExpanded = true
        notifyDataSetChanged() // 어댑터를 갱신하여 전체 아이템을 보여줌
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nickNameTextView: TextView = itemView.findViewById(R.id.user_nickname)
        private val ratingTextView: TextView = itemView.findViewById(R.id.user_rating)
        private val reviewTextView: TextView = itemView.findViewById(R.id.user_review)
        private val noReviewsTextView: TextView = itemView.findViewById(R.id.no_reviews_message)

        fun bindItems(item: ReviewItem) {
            noReviewsTextView.visibility = View.GONE // 리뷰가 있을 때 메시지 숨기기
            nickNameTextView.text = item.nickname
            ratingTextView.text = "★".repeat(item.grade) // 별점 출력
            reviewTextView.text = item.content
        }

        fun bindNoReviews() {
            nickNameTextView.visibility = View.GONE // 리뷰 정보 숨기기
            ratingTextView.visibility = View.GONE
            reviewTextView.visibility = View.GONE
            noReviewsTextView.visibility = View.VISIBLE // 메시지 보이기
        }
    }
}