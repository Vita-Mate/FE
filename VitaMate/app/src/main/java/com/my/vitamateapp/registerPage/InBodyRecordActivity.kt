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
import com.my.vitamateapp.databinding.ActivityInBodyRecordBinding

class InBodyRecordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInBodyRecordBinding
    private var isBmrValid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_in_body_record)

        // 초기 버튼 상태 설정
        binding.nextBtn.isEnabled = false

        // bmr 입력 필드의 텍스트 변경 리스너
        binding.bmrEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                validateBmrInput()
            }
        })

        // 다음 버튼 클릭 리스너
        binding.nextBtn.setOnClickListener {
            if (isBmrValid) {
                saveUserData()
                startActivity(Intent(this, RegistrationCompleteActivity::class.java))
                finish() // 현재 액티비티 종료
            } else {
                // 유효하지 않은 BMR 값이 입력된 경우
                Toast.makeText(this, "BMR 값을 1000 ~ 3000 사이로 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        // 이전 버튼 클릭 리스너
        binding.previousBtn.setOnClickListener {
            startActivity(Intent(this, InBodyCheckActivity::class.java))
            finish() // 현재 액티비티 종료
        }

        // BMR 입력 필드를 벗어났을 때 키패드를 내리고 유효성 검사를 수행
        binding.bmrEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // 입력 필드가 포커스를 잃었을 때
                validateBmrInput()
            }
        }
    }

    private fun validateBmrInput() {
        val bmrText = binding.bmrEditText.text.toString()
        isBmrValid = validateBmr(bmrText)
        if (!isBmrValid && bmrText.isNotEmpty()) {
            Toast.makeText(this, "정확한 BMR 값을 입력하시오.", Toast.LENGTH_SHORT).show()
        }
        binding.nextBtn.isEnabled = isBmrValid
        hideKeyboard()
    }

    private fun validateBmr(bmr: String): Boolean {
        val bmrValue = bmr.toIntOrNull()
        return bmrValue != null && bmrValue in 1000..3000
    }

    // 사용자 입력 데이터 저장 함수
    private fun saveUserData() {
        val sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val bmr = binding.bmrEditText.text.toString()

        editor.putString("bmr", bmr)
        editor.apply() // 변경 사항 저장

        // 저장된 데이터 확인 (문자열이 맞는지 확인)
        val savedBmr = sharedPreferences.getString("bmr", "없음")
        Toast.makeText(this, "저장된 BMR: $savedBmr", Toast.LENGTH_SHORT).show()
    }

    // 키패드 숨기기 함수
    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.bmrEditText.windowToken, 0)
    }
}