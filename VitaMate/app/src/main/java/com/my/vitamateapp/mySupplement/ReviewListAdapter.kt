package com.my.vitamateapp.mySupplement

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.my.vitamateapp.R

class ReviewListAdapter(private val items: MutableList<ReviewItem>) : RecyclerView.Adapter<ReviewListAdapter.ViewHolder>() {
    private var isExpanded = false // 전체보기 여부

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.review_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewListAdapter.ViewHolder, position: Int) {
        if (items.isEmpty()) {
            holder.bindNoReviews() // 리뷰가 없을 경우 메시지를 바인딩
        } else {
            if (position >= 0 && position < items.size) {
                holder.bindItems(items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        // isExpanded가 true면 전체 아이템을, false면 2개만 반환
        return if (isExpanded) items.size else minOf(items.size, 2)
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