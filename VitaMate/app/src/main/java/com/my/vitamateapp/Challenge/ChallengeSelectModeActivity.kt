package com.my.vitamateapp.Challenge

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.my.vitamateapp.HomeActivity
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.ActivityChallengeCreateorsearchBinding
import com.my.vitamateapp.databinding.ActivityChallengeSelectModeBinding
import com.my.vitamateapp.registerPage.MainActivity

class ChallengeSelectModeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChallengeSelectModeBinding
    private var selectedCategory: Category? = null  // 선택된 카테고리 저장

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_challenge_select_mode)

        // 전달받은 카테고리를 selectedCategory에 할당
        val categoryString = intent.getStringExtra("category")
        selectedCategory = categoryString?.let { Category.valueOf(it) }



        // 단체 버튼 클릭 시 단체 그룹 생성 페이지로 이동
        binding.modeGroup.setOnClickListener {
            mode_group()
        }

        // 개인 버튼 클릭 시 개인 챌린지 생성 페이지로 이동
        binding.modeInd.setOnClickListener {
            mode_ind()
        }

        // 닫기 버튼 클릭 시 현재 액티비티 종료
        binding.modeSelectCloseBtn.setOnClickListener {
            mode_select_close()
        }
    }

    // 단체 그룹 생성 페이지로 이동하는 함수
    private fun mode_group() {

        // Intent 생성 및 카테고리 전달
        val intent = Intent(this, ChallengeCreateOrSearchActivity::class.java).apply {
            putExtra("category", selectedCategory?.name)
        }
        startActivity(intent)
    }


    private fun mode_ind() {
        // Intent 생성 및 카테고리 전달
        val intent = Intent(this, ChallengeCreateIndividualActivity::class.java).apply {
            putExtra("category", selectedCategory?.name) // 선택된 카테고리 전달
        }
        startActivity(intent)
    }


    // 닫기 버튼 클릭 시 현재 액티비티 종료하는 함수
    private fun mode_select_close() {
        val intent = Intent(this, HomeActivity::class.java) // 현재 액티비티를 종료하고 홈화면으로 돌아감
        startActivity(intent)
    }
}
