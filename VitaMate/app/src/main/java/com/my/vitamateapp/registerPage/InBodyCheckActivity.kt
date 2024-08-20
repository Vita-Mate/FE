package com.my.vitamateapp.registerPage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.ActivityInBodyCheckBinding

class InBodyCheckActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInBodyCheckBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_body_check)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_in_body_check)

        //'네' 버튼 선택 시 인바디 기록 입력 창으로 이동
        binding.yesBtn.setOnClickListener{
            startActivity(Intent(this, InBodyRecordActivity::class.java))
            finish() // 현재 액티비티 종료..
        }

        //'아니오' 버튼 선택 시 키, 체중 입력 창으로 이동
        binding.noBtn.setOnClickListener{
            startActivity(Intent(this,  NoInBodyRecordActivity::class.java))
            finish() // 현재 액티비티 종료..
        }


    }
}