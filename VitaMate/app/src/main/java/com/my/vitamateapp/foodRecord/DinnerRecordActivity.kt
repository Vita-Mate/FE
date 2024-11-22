package com.my.vitamateapp.foodRecord

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.ActivityRecordDinnerBinding

class DinnerRecordActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRecordDinnerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_dinner)

        // < 버튼 클릭시 이전 페이지로 이동
        binding.preButton.setOnClickListener {
            goPre()
        }
    }

    //이전 페이지로 이동 함수
    private fun goPre() {
        finish()
    }

}