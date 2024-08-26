package com.my.vitamateapp.Challenge

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.my.vitamateapp.databinding.SearchChallGroupBinding

class ChallengeActivity : AppCompatActivity() {
    private lateinit var binding: SearchChallGroupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding 설정
        binding = SearchChallGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // RecyclerView 설정
        binding.challengeRecyclerView.layoutManager = LinearLayoutManager(this)

        // 데이터 초기화
        val challengeList = listOf(
            Challenge("Challenge 1", "Daily", "3"),
            Challenge("Challenge 2", "Weekly", "5"),
            Challenge("Challenge 3", "Monthly", "10")
        )

        // 어댑터 설정
        val adapter = ChallengeAdapter(challengeList)
        binding.challengeRecyclerView.adapter = adapter
    }
}
