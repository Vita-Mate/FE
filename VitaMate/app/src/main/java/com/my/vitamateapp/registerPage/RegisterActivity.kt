package com.my.vitamateapp.registerPage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.ActivityRegisterBinding
import com.my.vitamateapp.repository.MembersRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private var isMaleSelected = false
    private var isFemaleSelected = false
    private var isNicknameValid = false
    private var isDuplicateChecked = false
    private var isDateValid = false
    private lateinit var membersRepository: MembersRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        membersRepository = MembersRepository(this)


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
            checkNickname()
        }

        // 성별 버튼 클릭 리스너 설정
        binding.maleButton.setOnClickListener {
            isMaleSelected = true
            isFemaleSelected = false
            updateNextButtonState()
            hideKeyboard()
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
        val regex = "^[a-zA-Z가-힣]+$".toRegex() // 영어 대소문자와 한글만 허용
        if (!regex.matches(nickname)) {
            Toast.makeText(this, "닉네임은 한글 또는 영어만 입력할 수 있습니다.", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    //닉네임 중복 api 연동
    private fun checkNickname() {
        val nickname = binding.nicknameEditText.text.toString()
        Log.d("RegisterActivity", "중복 확인 시작: 닉네임 = $nickname")

        CoroutineScope(Dispatchers.IO).launch {
            val repository = MembersRepository(this@RegisterActivity)
            val isDuplicate = repository.checkNicknameDuplicate(nickname)

            withContext(Dispatchers.Main) {
                // 스낵바를 표시하여 사용자에게 결과를 알림
                val responseMessage = if (isDuplicate) {
                    "중복된 닉네임입니다."
                } else {
                    "사용 가능한 닉네임입니다."
                }

                showCustomSnackbar(responseMessage)

                isDuplicateChecked = !isDuplicate // 중복 여부 저장
                updateNextButtonState()
            }
        }
    }

    // 커스텀 스낵바 알람 띄우기 함수
    private fun showCustomSnackbar(message: String) {
        val snackbarView = layoutInflater.inflate(R.layout.custom_nicknamebar, null)

        val snackbarTextView = snackbarView.findViewById<TextView>(R.id.snackbar_text)
        snackbarTextView.text = message // 서버 응답 결과 메시지 설정 비타

        Toast(this).apply {
            duration = Toast.LENGTH_SHORT
            view = snackbarView
            setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 100) // 상단 중앙, Y 오프셋 100dp
        }.show()
    }

    private fun createDateTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val year = binding.yearEditText.text.toString()
                val month = binding.monthEditText.text.toString()
                val day = binding.dayEditText.text.toString()

                isDateValid = validateDate(year, month, day)
                updateNextButtonState()
            }

            override fun afterTextChanged(s: Editable?) {}
        }
    }

    private fun validateDate(year: String, month: String, day: String): Boolean {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val yearInt = year.toIntOrNull()
        val monthInt = month.toIntOrNull()
        val dayInt = day.toIntOrNull()

        val isYearValid = yearInt?.let { it in 1900..currentYear } ?: false
        val isMonthValid = monthInt?.let { it in 1..12 } ?: false
        val isDayValid = dayInt?.let { it in 1..daysInMonth(monthInt ?: 0, yearInt ?: 0) } ?: false

        return isYearValid && isMonthValid && isDayValid
    }

    private fun daysInMonth(month: Int, year: Int): Int {
        return when (month) {
            2 -> if (isLeapYear(year)) 29 else 28
            4, 6, 9, 11 -> 30
            else -> 31
        }
    }

    private fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }

    private fun handleCheckAction() {
        val year = binding.yearEditText.text.toString()
        val month = binding.monthEditText.text.toString()
        val day = binding.dayEditText.text.toString()

        isDateValid = validateDate(year, month, day)
        if (!isDateValid) {
            Toast.makeText(this, "정확한 생년월일을 입력하세요.", Toast.LENGTH_SHORT).show()
        }
        updateNextButtonState()
        hideKeyboard()
    }

    private fun updateNextButtonState() {
        binding.nextBtn.isEnabled = isNicknameValid && isDuplicateChecked && (isMaleSelected || isFemaleSelected) && isDateValid
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = currentFocus
        view?.let {
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun saveUserData() {
        val sharedPreferences = getSharedPreferences("saved_user_info", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val nickname = binding.nicknameEditText.text.toString()
        val year = binding.yearEditText.text.toString()
        val month = binding.monthEditText.text.toString()
        val day = binding.dayEditText.text.toString()
        val gender = if (isMaleSelected) 0 else 1

        editor.putString("nickname", nickname)
        editor.putString("birthDay", "$year-$month-$day")
        editor.putInt("gender", gender)
        editor.apply()

    }
}