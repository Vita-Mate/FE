package com.my.vitamateapp.Challenge

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.ActivityChallengeSelectModeBinding
import com.my.vitamateapp.registerPage.MainActivity

class ChallengeSelectModeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChallengeSelectModeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_challenge_select_mode)

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
        val intent = Intent(this, ChallengeCreateGroupActivity::class.java)
        startActivity(intent)
    }

    // 개인 챌린지 생성 페이지로 이동하는 함수
    private fun mode_ind() {
        val intent = Intent(this, ChallengeCreateIndividualActivity::class.java)
        startActivity(intent)
    }

    // 닫기 버튼 클릭 시 현재 액티비티 종료하는 함수
    private fun mode_select_close() {
        val intent = Intent(this, MainActivity::class.java) // 현재 액티비티를 종료하고 메인화면으로 돌아감
        startActivity(intent)
    }
}
