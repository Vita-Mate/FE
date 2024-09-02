package com.my.vitamateapp.mySupplement

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.my.vitamateapp.R

class ReviewListAdapter(private val items : MutableList<ReviewItemModel>) : RecyclerView.Adapter<ReviewListAdapter.ViewHolder>(){
    private var isExpanded = false // 전체보기 여부
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.review_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewListAdapter.ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    override fun getItemCount(): Int {
        // isExpanded가 true면 전체 아이템을, false면 2개만 반환
        return if (isExpanded) items.size else 2
    }

    //전체 아이템 보여주기 함수
    fun expandItems() {
        isExpanded = true
        notifyDataSetChanged() // 어댑터를 갱신하여 전체 아이템을 보여줌
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindItems(item: ReviewItemModel) {
            val nicknameTextView = itemView.findViewById<TextView>(R.id.user_nickname)
            val ratingTextView = itemView.findViewById<TextView>(R.id.user_rating)
            val reviewTextView = itemView.findViewById<TextView>(R.id.user_review)

            nicknameTextView.text = item.userNickname
            ratingTextView.text = "★".repeat(item.userRating.toInt())  // 별점 출력
            reviewTextView.text = item.userReview

        }

    }
}