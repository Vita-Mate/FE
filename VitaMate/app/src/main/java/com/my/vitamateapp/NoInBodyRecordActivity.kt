package com.my.vitamateapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.my.vitamateapp.databinding.ActivityNoInBodyRecordBinding

class NoInBodyRecordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoInBodyRecordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_in_body_record)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_no_in_body_record)

        binding.nextBtn.setOnClickListener{
            startActivity(Intent(this, RegistrationCompleteActivity::class.java))
            finish() // 현재 액티비티 종료..
        }

        binding.previousBtn.setOnClickListener{
            startActivity(Intent(this, InBodyCheckActivity::class.java))
            finish() // 현재 액티비티 종료..
        }

    }


}