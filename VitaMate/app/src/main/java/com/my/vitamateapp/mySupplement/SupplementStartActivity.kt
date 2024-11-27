package com.my.vitamateapp.mySupplement

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.my.vitamateapp.Api.RetrofitInstance
import com.my.vitamateapp.Api.SupplementsTakingApi
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.ActivitySupplementStartBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class SupplementStartActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySupplementStartBinding
    private val today = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 바인딩 초기화
        binding = ActivitySupplementStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 날짜 입력 변경 시마다 확인
        setupDateInputListeners()

        // SharedPreferences에서 영양제 이름 가져와서 툴바 텍스트뷰에 설정
        val supplementName = getSharedPreferences("saved_supplement_info", Context.MODE_PRIVATE)
            .getString("supplementName", "") // 기본값을 null로 설정
        binding.toolbar.findViewById<TextView>(R.id.supplement_name_detail).text = supplementName // null일 경우 빈 문자열로 설정됨


        // 완료 버튼 클릭 시 섭취 영양제로 추가
        binding.submitButton.setOnClickListener {
            submitSupplement()
            val intent = Intent(this, MySupplementActivity::class.java)
            startActivity(intent)
        }

        // < 버튼 클릭시 이전 페이지로 이동
        binding.preButton.setOnClickListener {
            goPre()
        }
    }

    private fun setupDateInputListeners() {
        val editTexts = listOf(binding.yearEditText, binding.monthEditText, binding.dayEditText)
        editTexts.forEach { editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    checkDateAndEnableButton()
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }
    }

    private fun submitSupplement() {
        val formattedDate = formatEnteredDate()
        Toast.makeText(this, "입력된 날짜: $formattedDate", Toast.LENGTH_SHORT).show()

        val year = binding.yearEditText.text.toString().toIntOrNull()
        val month = binding.monthEditText.text.toString().toIntOrNull()
        val day = binding.dayEditText.text.toString().toIntOrNull()

        if (year != null && month != null && day != null && isDateValid(year, month, day)) {
            takingSupplements(formattedDate)  // 영양제 추가 API 호출
        } else {
            Toast.makeText(this, "정확한 날짜를 입력하시오.", Toast.LENGTH_SHORT).show()
        }
    }

    // 날짜 유효성 확인 함수
    // 날짜 유효성 확인 함수
    private fun isDateValid(year: Int, month: Int, day: Int): Boolean {
        val inputDate = Calendar.getInstance().apply {
            set(year, month - 1, day) // month는 0부터 시작하므로 -1 해줌
            set(Calendar.HOUR_OF_DAY, 0) // 시간 정보 초기화
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // 오늘 날짜의 시간 정보를 초기화하여 비교
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)

        return inputDate <= today // 오늘 또는 이전 날짜일 경우 유효함
    }


    // 날짜가 올바른지 확인하고 버튼 활성화하는 함수
    private fun checkDateAndEnableButton() {
        val year = binding.yearEditText.text.toString().toIntOrNull()
        val month = binding.monthEditText.text.toString().toIntOrNull()
        val day = binding.dayEditText.text.toString().toIntOrNull()

        binding.submitButton.isEnabled = year != null && month != null && day != null && isDateValid(year, month, day)
    }

    // 날짜 포맷 함수
    private fun formatEnteredDate(): String {
        val year = binding.yearEditText.text.toString()
        val month = binding.monthEditText.text.toString().padStart(2, '0')
        val day = binding.dayEditText.text.toString().padStart(2, '0')
        return "$year-$month-$day"
    }

    // 영양제 추가 API 함수
    private fun takingSupplements(startTaking: String) {
        // SharedPreferences에서 액세스 토큰 가져오기
        val sharedPreferences = getSharedPreferences("saved_user_info", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("accessToken", null)
        Log.d("SupplementStartActivity", "Access token found: $accessToken")

        // SharedPreferences에서 추가할 영양제 ID 가져오기
        val supplementId = getSharedPreferences("saved_supplement_info", Context.MODE_PRIVATE)
            .getInt("supplementId", -1)

        if (accessToken.isNullOrEmpty() || supplementId == -1) {
            Toast.makeText(this, "액세스 토큰 또는 영양제 ID가 없습니다.", Toast.LENGTH_SHORT).show()
            Log.e("SupplementStartActivity", "Access token or supplementId is missing")
            return
        }

        val takingSupplementModel = TakingSupplementModel(startDate = startTaking)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Retrofit을 통해 API 호출
                val response = RetrofitInstance.getInstance()
                    .create(SupplementsTakingApi::class.java)
                    .takingSupplements("Bearer $accessToken", supplementId, takingSupplementModel)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        // 요청 성공 로그와 토스트
                        Log.d("SupplementStartActivity", "Supplement added successfully: ${response.body()}")
                        Toast.makeText(this@SupplementStartActivity, "영양제가 추가되었습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        // 요청 실패 로그와 토스트
                        Log.e("SupplementStartActivity", "Failed to add supplement: ${response.errorBody()?.string()}")
                        Toast.makeText(this@SupplementStartActivity, "영양제 추가 실패", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                // 예외 처리
                Log.e("SupplementStartActivity", "Error adding supplement: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@SupplementStartActivity, "네트워크 오류 발생", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //이전 페이지로 이동 함수
    private fun goPre() {
        finish()
    }
}