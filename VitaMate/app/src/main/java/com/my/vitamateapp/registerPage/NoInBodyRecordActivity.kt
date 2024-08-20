package com.my.vitamateapp.registerPage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.ActivityNoInBodyRecordBinding

class NoInBodyRecordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoInBodyRecordBinding
    private var isHeightValid = false
    private var isWeightValid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_no_in_body_record)

        // 초기 버튼 상태 설정
        binding.nextBtn.isEnabled = false

        // height 입력 필드의 텍스트 변경 리스너
        binding.heightEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                validateHeightInput()
            }
        })

        // weight 입력 필드의 텍스트 변경 리스너
        binding.weightEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                validateWeightInput()
            }
        })

        // 다음 버튼 클릭 리스너
        binding.nextBtn.setOnClickListener {
            if (isHeightValid && isWeightValid) {
                saveUserData()
                startActivity(Intent(this, RegistrationCompleteActivity::class.java))
                finish() // 현재 액티비티 종료
            }
        }

        // 이전 버튼 클릭 리스너
        binding.previousBtn.setOnClickListener {
            startActivity(Intent(this, InBodyCheckActivity::class.java))
            finish() // 현재 액티비티 종료
        }
    }

    private fun validateHeightInput() {
        val heightText = binding.heightEditText.text.toString()
        isHeightValid = validateHeight(heightText)
        if (!isHeightValid) {
            // 유효하지 않을 때 키패드가 내려가면 토스트 메시지 표시
            hideKeyboard()
            Toast.makeText(this, "정확한 키를 입력하시오.", Toast.LENGTH_SHORT).show()
        }
        checkFormValidity()
    }

    private fun validateWeightInput() {
        val weightText = binding.weightEditText.text.toString()
        isWeightValid = validateWeight(weightText)
        if (!isWeightValid) {
            // 유효하지 않을 때 키패드가 내려가면 토스트 메시지 표시
            hideKeyboard()
            Toast.makeText(this, "정확한 몸무게를 입력하시오.", Toast.LENGTH_SHORT).show()
        }
        checkFormValidity()
    }

    private fun validateHeight(height: String): Boolean {
        val heightValue = height.toIntOrNull()
        return heightValue != null && heightValue in 100..250
    }

    private fun validateWeight(weight: String): Boolean {
        val weightValue = weight.toIntOrNull()
        return weightValue != null && weightValue in 30..200
    }

    private fun checkFormValidity() {
        binding.nextBtn.isEnabled = isHeightValid && isWeightValid
    }

    // 사용자 입력 데이터 저장 함수
    private fun saveUserData() {
        val sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val height = binding.heightEditText.text.toString()
        val weight = binding.weightEditText.text.toString()

        editor.putString("height", height)
        editor.putString("weight", weight)
        editor.apply() // 변경 사항 저장

        // 저장된 데이터 확인 (문자열이 맞는지 확인)
        val savedHeight = sharedPreferences.getString("height", "없음")
        val savedWeight = sharedPreferences.getString("weight", "없음")
        Toast.makeText(this, "저장된 키: $savedHeight, 저장된 몸무게: $savedWeight", Toast.LENGTH_SHORT).show()
    }

    // 키패드 숨기기 함수
    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = currentFocus
        view?.let {
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}