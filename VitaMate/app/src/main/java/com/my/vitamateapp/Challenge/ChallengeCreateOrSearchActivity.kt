package com.my.vitamateapp.Challenge

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.ActivityChallengeCreateGroupBinding
import com.my.vitamateapp.databinding.ActivityChallengeCreateorsearchBinding
import com.my.vitamateapp.databinding.ActivityChallengeSelectModeBinding

class ChallengeCreateOrSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChallengeCreateorsearchBinding
    private var selectedCategory: Category? = null  // 선택된 카테고리 저장

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_challenge_createorsearch)

        // 전달받은 카테고리를 selectedCategory에 할당
        val categoryString = intent.getStringExtra("category")
        selectedCategory = categoryString?.let { Category.valueOf(it) }

        binding.createChallenge.setOnClickListener{
            create_challenge()
        }

        binding.searchChallenge.setOnClickListener{
            search_challenge()
        }

}
    private fun create_challenge() {
        // Intent 생성 및 카테고리 전달
        val intent = Intent(this, ChallengeCreateGroupActivity::class.java).apply {
            putExtra("category", selectedCategory?.name)
        }
        startActivity(intent)

    }
    private fun search_challenge() {
        val intent = Intent(this, ChallengeSearchGroup::class.java).apply {
            putExtra("category", selectedCategory?.name)
        }
        startActivity(intent)
    }}