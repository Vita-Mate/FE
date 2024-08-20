package com.my.vitamateapp.registerPage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.my.vitamateapp.HomeActivity
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.ActivityRegistrationCompleteBinding

class RegistrationCompleteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationCompleteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_complete)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_registration_complete)

        //네개의 체크박스가 모두 선택됐을 경우, 다음 버튼이 활성화되어 페이지 전환.

        val checkbox1 = binding.checkBox1
        val checkbox2 = binding.checkBox2
        val checkbox3 = binding.checkBox3
        val checkbox4 = binding.checkBox4
        val nextBtn = binding.nextBtn

        // 체크박스 상태 변화 감지
        checkbox1.setOnCheckedChangeListener { _, isChecked -> checkIfAllChecked() }
        checkbox2.setOnCheckedChangeListener { _, isChecked -> checkIfAllChecked() }
        checkbox3.setOnCheckedChangeListener { _, isChecked -> checkIfAllChecked() }
        checkbox4.setOnCheckedChangeListener { _, isChecked -> checkIfAllChecked() }

        // 초기 상태 업데이트
        checkIfAllChecked()

        // 다음 버튼 클릭 리스너
        nextBtn.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish() // 현재 액티비티 종료.. HOME 화면으로
        }
    }

    // 모든 체크박스가 선택되었는지 확인하여 버튼 활성화
    private fun checkIfAllChecked() {
        val checkbox1 = binding.checkBox1
        val checkbox2 = binding.checkBox2
        val checkbox3 = binding.checkBox3
        val checkbox4 = binding.checkBox4
        val nextBtn = binding.nextBtn

        nextBtn.isEnabled = checkbox1.isChecked && checkbox2.isChecked && checkbox3.isChecked && checkbox4.isChecked
    }
}
