package com.my.vitamateapp.Challenge

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.my.vitamateapp.R

// Challenge 데이터를 담는 클래스 정의 (예시)
data class Challenge(
    val title: String,
    val frequency: String,
    val dDay: String
)

class ChallengeAdapter(private val challengeList: List<Challenge>) :
    RecyclerView.Adapter<ChallengeAdapter.ChallengeViewHolder>() {

    // ViewHolder 정의
    class ChallengeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val frequency: TextView = view.findViewById(R.id.frequency)
        val dDay: TextView = view.findViewById(R.id.dDay)
        val joinButton: Button = view.findViewById(R.id.joinButton)
    }

    // ViewHolder 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_challenge, parent, false)
        return ChallengeViewHolder(itemView)
    }

    // ViewHolder에 데이터 바인딩
    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        val challenge = challengeList[position]
        holder.title.text = challenge.title
        holder.frequency.text = challenge.frequency
        holder.dDay.text = "D-day: ${challenge.dDay}"

        holder.joinButton.setOnClickListener {
            // 참여하기 버튼 클릭 처리
            Toast.makeText(holder.itemView.context, "${challenge.title} 참여하기 클릭!", Toast.LENGTH_SHORT).show()
        }
    }

    // 리스트 아이템 개수 반환
    override fun getItemCount() = challengeList.size
}

