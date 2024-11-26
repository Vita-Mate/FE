package com.my.vitamateapp.Challenge

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.my.vitamateapp.Api.ParticipatingChallengeApi
import com.my.vitamateapp.Api.RetrofitInstance
import com.my.vitamateapp.ChallengeDTO.Category
import com.my.vitamateapp.ChallengeDTO.ChallengePreviewResponse
import com.my.vitamateapp.HomeActivity
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.ActivityChallengeSelectModeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChallengeSelectModeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChallengeSelectModeBinding
    private var selectedCategory: Category? = null  // 선택된 카테고리 저장
    private var startDate: String? = null
    private var dday: Int? = null  // dday 값을 저장할 변수

    private val participatingApi: ParticipatingChallengeApi = RetrofitInstance.getInstance().create(
        ParticipatingChallengeApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_challenge_select_mode)

        // 전달받은 카테고리 이름을 Category Enum으로 변환
        val categoryString = intent.getStringExtra("category")
        Log.d(TAG, "Received category: $categoryString")

        // Intent에서 dday 값을 받기
        dday = intent.getIntExtra("dday", -1)  // 기본값 -1 (값이 없을 경우)

        startDate = intent.getStringExtra("startDate")  // startDate (디데이) 수신

        selectedCategory = try {
            categoryString?.let { Category.valueOf(it) }
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, "Invalid category: $categoryString", e)
            null  // 유효하지 않은 카테고리일 경우 null 처리
        }

        fetchChallengeData()

        // 버튼 클릭 시 상태 업데이트
        updateChallengeButton()

        // 닫기 버튼 클릭 시 현재 액티비티 종료
        binding.modeSelectCloseBtn.setOnClickListener {
            mode_select_close()
        }

        binding.modeInd.setOnClickListener {
            mode_ind()
        }
    }

    private fun fetchChallengeData() {
        if (selectedCategory == null) {
            showToast("카테고리를 선택해 주세요.")
            return
        }

        val accessToken = getAccessToken(this) ?: run {
            showToast("Access Token이 없습니다.")
            return
        }

        participatingApi.ParticipatingList("Bearer $accessToken", selectedCategory?.name ?: "")
            .enqueue(object : Callback<ChallengePreviewResponse> {
                override fun onResponse(
                    call: Call<ChallengePreviewResponse>,
                    response: Response<ChallengePreviewResponse>
                ) {
                    if (response.isSuccessful && response.body()?.isSuccess == false) {
                        val challengeData = response.body()
                        if (challengeData != null && challengeData.isSuccess == false) {
                            val participating = challengeData.result
                            dday = participating.dday
                            startDate = participating.startDate

                            // challengeId 저장 로직 추가
                            val sharedPref = getSharedPreferences("ChallengePrefs", MODE_PRIVATE)
                            sharedPref.edit().putLong("challengeId_${selectedCategory?.name}", participating.challengeId).apply()


                            Log.d("ChallengeSelectModeActivity", "dday: $dday, startDate: $startDate")
                        } else {
                            Log.e("ChallengeSelectModeActivity", "Error: ${challengeData?.message}")
                            showToast("챌린지 정보를 불러오는 데 실패했습니다.")
                        }
                    } else {
                        Log.e("ChallengeSelectModeActivity", "API 호출 실패: ${response.errorBody()?.string()}")
                        showToast("챌린지 정보를 불러오는 데 실패했습니다.")
                    }
                }

                override fun onFailure(call: Call<ChallengePreviewResponse>, t: Throwable) {
                    Log.e("ChallengeSelectModeActivity", "API 호출 실패: ${t.message}")
                    showToast("네트워크 오류가 발생했습니다.")
                }
            })
    }

    private fun isParticipatingInChallenge(key: String?): Boolean {
        val sharedPreferences = getSharedPreferences("ChallengePrefs", MODE_PRIVATE)
        return sharedPreferences.getBoolean(key, false)
    }

    private fun updateChallengeButton() {
        val participationKey = "isParticipatingIn${selectedCategory?.name}"
        val isParticipating = isParticipatingInChallenge(participationKey)

        // 참여한 챌린지가 있으면 단체 그룹 생성 페이지로 이동
        binding.modeGroup.setOnClickListener {
            mode_group()
        }
        binding.modeInd.setOnClickListener{
            mode_ind()
        }
    }

    private fun mode_group() {
        val challengeId = getChallengeIdByCategory(selectedCategory)  // 카테고리별 challengeId 가져오기

        val intent = Intent(this, ChallengeCreateOrSearchActivity::class.java).apply {
            selectedCategory?.let { putExtra("category", it.name) }  // Category 이름 전달
            challengeId?.let { putExtra("challengeId", it) }  // challengeId 전달
            // 디데이 확인 (startDate가 null이 아닌 경우)
            startDate?.let {
                Log.d("ChallengeSelectModeActivity", "startDate: $it")
                putExtra("startDate", startDate)
            }
            dday?.let {
                putExtra("dday", it)  // dday 값을 전달
            }
            Log.d(TAG, "Sending to ChallengeCreateOrSearchActivity: challengeId=$challengeId, Category=${selectedCategory?.name}, StartDate=$startDate, dday=$dday")
        }
        startActivity(intent)
    }

    private fun mode_ind() {
        val challengeId = getChallengeIdByCategory(selectedCategory)  // 카테고리별 challengeId 가져오기

        val intent = Intent(this, ChallengePersonalCreateOrMyPageActivity::class.java).apply {
            selectedCategory?.let { putExtra("category", it.name) }  // Category 이름 전달
            challengeId?.let { putExtra("challengeId", it) }  // challengeId 전달
            // 디데이 확인 (startDate가 null이 아닌 경우)
            startDate?.let {
                Log.d("ChallengeSelectModeActivity", "startDate: $it")
                putExtra("startDate", startDate)
            }
            dday?.let {
                putExtra("dday", it)  // dday 값을 전달
            }
            Log.d(TAG, "Sending to ChallengePersonalCreateOrSearchActivity: challengeId=$challengeId, Category=${selectedCategory?.name}, StartDate=$startDate, dday=$dday")
        }
        startActivity(intent)
    }

    private fun mode_select_close() {
        val intent = Intent(this, HomeActivity::class.java) // 현재 액티비티를 종료하고 홈화면으로 돌아감
        startActivity(intent)
    }

    private fun getChallengeIdByCategory(category: Category?): Long? {
        if (category == null) return null
        val sharedPref = getSharedPreferences("ChallengePrefs", MODE_PRIVATE)
        val key = "challengeId_${category.name}"  // 카테고리별 키
        return sharedPref.getLong(key, -1L).takeIf { it != -1L }
    }

    private fun getAccessToken(context: Context): String? {
        return context.getSharedPreferences("saved_user_info", Context.MODE_PRIVATE)
            .getString("accessToken", null)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
