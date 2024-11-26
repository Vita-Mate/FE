package com.my.vitamateapp.Challenge

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
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.ActivityChallengeCreateorsearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChallengeCreateOrSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChallengeCreateorsearchBinding
    private var selectedCategory: Category? = null
    private var challengeId: Long? = null
    private var startDate: String? = null
    private var dday: Int? = null

    private val participatingApi: ParticipatingChallengeApi =
        RetrofitInstance.getInstance().create(ParticipatingChallengeApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_challenge_createorsearch)

        // Intent로 전달된 데이터 가져오기
        selectedCategory = intent.getStringExtra("category")?.let { Category.valueOf(it) }

        binding.closeBtn.setOnClickListener { finish() }
        updateChallengeButton()  // 챌린지 ID를 기반으로 버튼 업데이트

        binding.searchChallenge.setOnClickListener { searchChallenge() }
    }

    override fun onResume() {
        super.onResume()
        updateChallengeButton()  // 챌린지 상태가 갱신될 때마다 버튼 업데이트
    }

    private fun updateChallengeButton() {

        val challengeId = getChallengeIdByCategory(selectedCategory)  // 카테고리별 challengeId 가져오기

        // challengeId 유효성 확인 후 버튼 설정
        if (challengeId != null && isValidChallengeId(challengeId!!)) {

            setupMyPageButton() // "마이 페이지" 버튼 설정
        } else {
            setupCreateChallengeButton() // "챌린지 만들기" 버튼 설정
        }
    }

    private fun setupCreateChallengeButton() {
        binding.createChallenge.text = "챌린지 만들기"
        binding.createChallenge.setOnClickListener { createChallenge() }
    }

    private fun setupMyPageButton() {
        fetchChallengeData() // 최신 데이터 동기화
        binding.createChallenge.text = "마이 페이지"
        binding.createChallenge.setOnClickListener {
            challengeId?.let { goToMyPage(it) }
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
                        val participating = response.body()?.result
                        if (participating != null) {
                            dday = participating.dday
                            startDate = participating.startDate
                            challengeId = participating.challengeId



                            // challengeId 저장
                            selectedCategory?.let { saveChallengeIdToPreferences(it, challengeId!!) }
                            // API 응답 후 버튼 설정
                            updateChallengeButton()
                        }
                    } else {
                        handleError(response)
                    }
                }

                override fun onFailure(call: Call<ChallengePreviewResponse>, t: Throwable) {
                    Log.e("ChallengeCreateOrSearch", "API 호출 실패: ${t.message}")
                    showToast("네트워크 오류가 발생했습니다.")
                }
            })
    }

    private fun handleError(response: Response<ChallengePreviewResponse>) {
        val errorMsg = response.body()?.message ?: "챌린지 정보를 불러오는 데 실패했습니다."
        Log.e("ChallengeCreateOrSearch", "Error: $errorMsg")
        showToast(errorMsg)
    }

    private fun createChallenge() {
        val intent = Intent(this, ChallengeCreateGroupActivity::class.java).apply {
            selectedCategory?.let { putExtra("category", it.name) }
        }
        startActivity(intent)
    }

    private fun searchChallenge() {
        val intent = Intent(this, ChallengeSearchGroup::class.java).apply {
            selectedCategory?.let { putExtra("category", it.name) }
        }
        startActivity(intent)
    }

    private fun goToMyPage(challengeId: Long) {
        if (selectedCategory == null) {
            showToast("카테고리가 선택되지 않았습니다.")
            return
        }

        // dday가 0 이하일 때만 마이 페이지로 이동하도록 조건 변경
        if (dday != null && dday!! <= 0) {
            val intent = when (selectedCategory) {
                Category.EXERCISE -> Intent(this, ChallengeMyExercisePageActivity::class.java)
                Category.QUIT_ALCOHOL -> Intent(this, ChallengeMyNoDrinkPageActivity::class.java)
                Category.QUIT_SMOKE -> Intent(this, ChallengeMyNoSmokePageActivity::class.java)
                else -> null
            }?.apply { putExtra("challengeId", challengeId) }

            intent?.let { startActivity(it) } ?: showToast("올바른 카테고리가 아닙니다.")
        } else {
            showToast("챌린지가 시작되지 않았거나, 아직 진행되지 않았습니다.")
        }
    }


    private fun getAccessToken(context: Context): String? {
        return context.getSharedPreferences("saved_user_info", Context.MODE_PRIVATE)
            .getString("accessToken", null)
    }

    private fun saveChallengeIdToPreferences(category: Category, challengeId: Long) {
        getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
            .putLong("challengeId_${category.name}", challengeId)
            .apply()
        }

    private fun getChallengeIdByCategory(category: Category?): Long? {
        if (category == null) return null
        val sharedPref = getSharedPreferences("ChallengePrefs", MODE_PRIVATE)
        val key = "challengeId_${category.name}"  // 카테고리별 키
        return sharedPref.getLong(key, -1L).takeIf { it != -1L }
    }

    private fun isValidChallengeId(challengeId: Long): Boolean {
        return challengeId > 0 // 0보다 크면 유효
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val PREF_NAME = "ChallengePrefs"
        private const val DEFAULT_CHALLENGE_ID = -1L
    }
}

