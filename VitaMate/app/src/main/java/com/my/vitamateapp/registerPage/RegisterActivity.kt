package com.my.vitamateapp.registerPage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.ActivityRegisterBinding
import java.util.Calendar

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private var isMaleSelected = false
    private var isFemaleSelected = false
    private var isNicknameValid = false
    private var isDuplicateChecked = false
    private var isDateValid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        // 초기 버튼 상태 설정
        binding.checkDuplicateBtn.isEnabled = false
        binding.nextBtn.isEnabled = false

        // 닉네임 입력 필드의 텍스트 변경 리스너
        binding.nicknameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s.toString()
                isNicknameValid = validateNickname(text)
                binding.checkDuplicateBtn.isEnabled = isNicknameValid
                updateNextButtonState()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // 중복 확인 버튼 클릭 리스너
        binding.checkDuplicateBtn.setOnClickListener {
            // 중복 확인 로직이 들어가야 합니다. 여기서는 가정으로 활성화 상태로 설정합니다.
            isDuplicateChecked = true
            updateNextButtonState()
            hideKeyboard()
        }

        // 성별 버튼 클릭 리스너 설정
        binding.maleButton.setOnClickListener {
            isMaleSelected = true
            isFemaleSelected = false
            updateNextButtonState()
        }

        binding.femaleButton.setOnClickListener {
            isFemaleSelected = true
            isMaleSelected = false
            updateNextButtonState()
        }

        // 날짜 입력 필드의 텍스트 변경 리스너
        val dateTextWatcher = createDateTextWatcher()
        binding.yearEditText.addTextChangedListener(dateTextWatcher)
        binding.monthEditText.addTextChangedListener(dateTextWatcher)
        binding.dayEditText.addTextChangedListener(dateTextWatcher)

        // 체크 버튼 클릭 리스너 설정
        binding.yearEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                handleCheckAction()
                true
            } else {
                false
            }
        }
        binding.monthEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                handleCheckAction()
                true
            } else {
                false
            }
        }
        binding.dayEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                handleCheckAction()
                true
            } else {
                false
            }
        }

        // 다음 버튼 클릭 리스너 설정
        binding.nextBtn.setOnClickListener {
            // 데이터 저장하기
            saveUserData()
            startActivity(Intent(this, InBodyCheckActivity::class.java))
            finish() // 현재 액티비티 종료
        }
    }

    private fun validateNickname(nickname: String): Boolean {
        // 정규 표현식을 사용하여 한글만 허용 (영어, 숫자, 특수문자 제외)
        val regex = "^[가-힣]+$".toRegex()
        if (!regex.matches(nickname)) {
            Toast.makeText(this, "닉네임은 한글만 입력할 수 있습니다.", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun createDateTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val year = binding.yearEditText.text.toString()
                val month = binding.monthEditText.text.toString()
                val day = binding.dayEditText.text.toString()

                // 날짜 유효성 검사 및 버튼 상태 업데이트
                isDateValid = validateDate(year, month, day)
                updateNextButtonState()
            }

            override fun afterTextChanged(s: Editable?) {}
        }
    }

    private fun validateDate(year: String, month: String, day: String): Boolean {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)

        // 입력값을 정수로 변환
        val yearInt = year.toIntOrNull()
        val monthInt = month.toIntOrNull()
        val dayInt = day.toIntOrNull()

        // 연도, 월, 일 입력값 검증
        val isYearValid = yearInt?.let { it in 1900..currentYear } ?: false
        val isMonthValid = monthInt?.let { it in 1..12 } ?: false
        val isDayValid = dayInt?.let { it in 1..daysInMonth(monthInt ?: 0, yearInt ?: 0) } ?: false

        return isYearValid && isMonthValid && isDayValid
    }

    // 주어진 월과 연도에 따른 일 수를 반환
    private fun daysInMonth(month: Int, year: Int): Int {
        return when (month) {
            2 -> if (isLeapYear(year)) 29 else 28
            4, 6, 9, 11 -> 30
            else -> 31
        }
    }

    // 윤년 여부 확인
    private fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }

    private fun handleCheckAction() {
        val year = binding.yearEditText.text.toString()
        val month = binding.monthEditText.text.toString()
        val day = binding.dayEditText.text.toString()

        // 생년월일 유효성 검사
        isDateValid = validateDate(year, month, day)

        // 모든 입력값 유효성 검사 후 다음 버튼 활성화
        if (!isDateValid) {
            Toast.makeText(this, "정확한 생년월일을 입력하세요.", Toast.LENGTH_SHORT).show()
        }
        updateNextButtonState()

        // 유효성 검증 후 키패드 내려주기
        hideKeyboard()
    }

    private fun updateNextButtonState() {
        // 닉네임 유효성, 중복 확인 상태, 성별 선택 여부, 생년월일 입력 유효성 확인
        binding.nextBtn.isEnabled = isNicknameValid && isDuplicateChecked && (isMaleSelected || isFemaleSelected) && isDateValid
    }

    // 키패드 내리기 함수
    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = currentFocus
        view?.let {
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    // 사용자 입력 데이터 저장 함수
    private fun saveUserData() {
        val sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val nickname = binding.nicknameEditText.text.toString()
        val year = binding.yearEditText.text.toString()
        val month = binding.monthEditText.text.toString()
        val day = binding.dayEditText.text.toString()
        val gender = if (isMaleSelected) "male" else "female"

        editor.putString("nickname", nickname)
        editor.putString("birthdate", "$year-$month-$day")
        editor.putString("gender", gender)
        editor.apply() // 변경 사항 저장

        // 저장된 데이터 확인 (문자열이 맞는지 확인)
        val savedNickname = sharedPreferences.getString("nickname", "없음")
        val savedBirthdate = sharedPreferences.getString("birthdate", "없음")
        val savedGender = sharedPreferences.getString("gender", "없음")
        Toast.makeText(this, "닉네임: $savedNickname, 생년월일: $savedBirthdate, 성별: $savedGender", Toast.LENGTH_SHORT).show()
    }
}