package com.my.vitamateapp.mySupplement

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.ActivitySupplementDetailBinding
import com.my.vitamateapp.databinding.ActivityWriteReviewBinding
import com.my.vitamateapp.repository.SupplementsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WriteReviewActivity : AppCompatActivity() {

    private var grade: Int = 0 // 별점 초기화
    private lateinit var reviewInput: EditText
    private lateinit var submitButton: Button
    private lateinit var starViews: List<TextView>
    private lateinit var supplementsRepository: SupplementsRepository
    private lateinit var binding: ActivityWriteReviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // binding 초기화 및 레이아웃 설정
        binding = ActivityWriteReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // SharedPreferences에서 저장된 영양제 이름 가져오기
        val sharedPref = getSharedPreferences("saved_supplement_info", Context.MODE_PRIVATE)
        val supplementName = sharedPref.getString("supplementName", "영양제 이름") // 기본값은 "영양제 이름"

        // 로그 추가하여 값 확인
        Log.d("WriteReviewActivity", "불러온 영양제 이름: $supplementName")

        // 가져온 영양제 이름을 TextView에 설정 비타
        binding.supplementNameDetail.text = supplementName


        reviewInput = findViewById(R.id.review_input)
        submitButton = findViewById(R.id.submit_button)

        // 별점 뷰 리스트 초기화
        starViews = listOf(
            findViewById(R.id.star1),
            findViewById(R.id.star2),
            findViewById(R.id.star3),
            findViewById(R.id.star4),
            findViewById(R.id.star5)
        )

        // SupplementsRepository 초기화
        supplementsRepository = SupplementsRepository(this)

        // 별점 클릭 리스너 설정
        for (i in starViews.indices) {
            starViews[i].setOnClickListener {
                grade = i + 1 // 클릭한 별점에 따라 값 설정
                updateStarDisplay(i) // 별점 표시 업데이트
            }
        }

        // 작성완료 버튼 클릭
        submitButton.setOnClickListener {
            hideKeyboard()
            writeSupplementsReview() // 리뷰 제출 함수 호출 최고네요 짱짱

            //영양제 상세정보 페이지로 다시 이동
            val intent = Intent(this, SupplementDetailActivity::class.java)
            startActivity(intent)
        }

        // < 버튼 클릭시 이전 페이지로 이동
        binding.preButton.setOnClickListener {
            goPre()
        }
    }

    private fun updateStarDisplay(selectedIndex: Int) {
        for (i in starViews.indices) {
            starViews[i].text = if (i <= selectedIndex) {
                // 꽉 찬 별 모양
                "\u2605"
            } else {
                // 빈 별 모양
                "\u2606"
            }
            // 색상 변경
            starViews[i].setTextColor(
                if (i <= selectedIndex) {
                    resources.getColor(android.R.color.holo_orange_light)
                } else {
                    resources.getColor(android.R.color.darker_gray)
                }
            )
        }
    }

    // 리뷰 작성 API 연동
    private fun writeSupplementsReview() {
        val sharedPreferences = getSharedPreferences("saved_user_info", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("accessToken", null)

        if (accessToken.isNullOrEmpty()) {
            Log.e("WriteReviewActivity", "액세스 토큰이 유효하지 않습니다.")
            return
        }

        // 저장된 영양제 ID 가져오기
        val supplementId = getSavedSupplementId()

        // 리뷰 내용 가져오기
        val reviewContent = reviewInput.text.toString()
        if (reviewContent.isBlank()) {
            Log.e("WriteReviewActivity", "리뷰 내용을 입력해주세요.")
            return
        }

        // WriteReviewRequest 모델 생성
        val reviewRequest = WriteReviewRequest(grade, reviewContent)
        // 별점과 리뷰 내용을 로그로 출력
        Log.d("WriteReviewActivity", "리뷰 작성 정보 - 별점: $grade, 내용: $reviewContent")


        // 코루틴을 통해 API 호출
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // Boolean 값을 반환받음
                val isSuccess = supplementsRepository.writeSupplementReview(accessToken, supplementId, reviewRequest)
                withContext(Dispatchers.Main) {
                    if (isSuccess) {
                        Log.i("WriteReviewActivity", "리뷰가 성공적으로 작성되었습니다.")
                        finish() // 성공 시 액티비티 종료
                    } else {
                        Log.e("WriteReviewActivity", "리뷰 작성에 실패했습니다.")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("WriteReviewActivity", "리뷰 작성 중 오류 발생: ${e.message}")
                }
            }
        }
    }


    // 저장된 영양제 ID를 가져오는 함수
    private fun getSavedSupplementId(): Int {
        val sharedPref = getSharedPreferences("saved_supplement_info", Context.MODE_PRIVATE)
        val supplementId = sharedPref.getInt("supplementId", -1) // 기본값 -1 (저장된 ID가 없을 때)

        // 로그 추가
        if (supplementId != -1) {
            Log.d("WriteReviewActivity", "저장된 영양제 ID: $supplementId")
        } else {
            Log.d("WriteReviewActivity", "저장된 영양제 ID가 없습니다.")
        }

        return supplementId
    }

    //이전 페이지로 이동 함수
    private fun goPre() {
        finish()
    }

    //키보드 내리기 함수
    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.let {
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}