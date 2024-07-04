package com.my.vitamateapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.my.vitamateapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {


    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //모든 정보를 입력 해야 다음 버튼 활성화 -> 페이지 전환.

        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)



        binding.nextBtn.setOnClickListener{
            startActivity(Intent(this, InBodyCheckActivity::class.java))
            finish() // 현재 액티비티 종료..
        }


    }
}