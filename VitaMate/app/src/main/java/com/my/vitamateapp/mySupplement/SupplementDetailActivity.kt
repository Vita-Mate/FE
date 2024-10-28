package com.my.vitamateapp.mySupplement

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.my.vitamateapp.databinding.ActivitySupplementDetailBinding


class SupplementDetailActivity : AppCompatActivity() {

    // View Binding 객체
    private lateinit var binding: ActivitySupplementDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // View Binding 객체 초기화
        binding = ActivitySupplementDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intent로부터 데이터 추출
        val supplementName = intent.getStringExtra("supplement_name")
        val supplementInfo = intent.getStringExtra("supplement_info")

        // UI에 데이터 설정
        binding.supplementNameDetail.text = supplementName
        binding.supplementInfoDetail.text = supplementInfo

        //추가버튼 클릭 시 복용시작액티비티로 이동
        binding.addButton.setOnClickListener(){
            val intent = Intent(this, SupplementStartActivity::class.java)
            startActivity(intent)
        }
    }
}


