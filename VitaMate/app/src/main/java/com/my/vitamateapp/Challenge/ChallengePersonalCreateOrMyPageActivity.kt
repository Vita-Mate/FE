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
import com.my.vitamateapp.databinding.ActivityChallengePersonalCreateormypageBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChallengePersonalCreateOrMyPageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChallengePersonalCreateormypageBinding
    private var selectedCategory: Category? = null
    private var challengeId: Long? = null
    private var startDate: String? = null
    private var dday: Int? = null

    private val participatingApi: ParticipatingChallengeApi =
        RetrofitInstance.getInstance().create(ParticipatingChallengeApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_challenge_personal_createormypage)

        // Intent로 전달된 데이터 가져오기
        selectedCategory = intent.getStringExtra("category")?.let { Category.valueOf(it) }
        challengeId = intent.getLongExtra("challengeId", -1L)

        binding.closeBtn.setOnClickListener { finish() }
        updateChallengeButton()
    }

    override fun onResume() {
        super.onResume()
        updateChallengeButton()
    }

    private fun updateChallengeButton() {
        challengeId = selectedCategory?.let { getChallengeIdFromPreferences(it) }

        if (!isValidChallengeId(challengeId!!)) {
            setupCreateChallengeButton()
        } else {
            setupMyPageButton()
        }
    }


    private fun setupCreateChallengeButton() {
        binding.createPersonalChallenge.text = "만들기"
        binding.createPersonalChallenge.setOnClickListener { createChallenge() }
    }

    private fun setupMyPageButton() {
        fetchChallengeData()
        binding.createPersonalChallenge.text = "마이 페이지"
        binding.createPersonalChallenge.setOnClickListener { challengeId?.let { goToMyPage(it) } }
    }

    private fun fetchChallengeData() {
        val accessToken = getAccessToken(this) ?: run {
            showToast("Access Token이 없습니다.")
            return
        }

        selectedCategory?.let { category ->
            participatingApi.ParticipatingList("Bearer $accessToken", category.name)
                .enqueue(object : Callback<ChallengePreviewResponse> {
                    override fun onResponse(
                        call: Call<ChallengePreviewResponse>,
                        response: Response<ChallengePreviewResponse>
                    ) {
                        if (response.isSuccessful && response.body()?.isSuccess == false) {
                            response.body()?.result?.let { result ->
                                dday = result.dday
                                startDate = result.startDate
                                Log.d("ChallengePersonalActivity", "dday: $dday, startDate: $startDate")
                            } ?: showToast("챌린지 정보를 불러오는 데 실패했습니다.")
                        } else {
                            handleError(response)
                        }
                    }

                    override fun onFailure(call: Call<ChallengePreviewResponse>, t: Throwable) {
                        Log.e("ChallengePersonalActivity", "API 호출 실패: ${t.message}")
                        showToast("네트워크 오류가 발생했습니다.")
                    }
                })
        } ?: showToast("카테고리를 선택해 주세요.")
    }

    private fun handleError(response: Response<ChallengePreviewResponse>) {
        val errorMsg = response.body()?.message ?: "챌린지 정보를 불러오는 데 실패했습니다."
        Log.e("ChallengePersonalActivity", "Error: $errorMsg")
        showToast(errorMsg)
    }

    private fun createChallenge() {
        val intent = Intent(this, ChallengeCreatePersonalActivity::class.java).apply {
            selectedCategory?.let { putExtra("category", it.name) }
        }
        startActivity(intent)
    }


    private fun goToMyPage(challengeId: Long) {
        if (selectedCategory == null) {
            showToast("카테고리가 선택되지 않았습니다.")
            return
        }

        if (dday != null && dday!! > 0) {
            showToast("아직 챌린지가 시작되지 않았습니다.")
            return
        }

        val intent = when (selectedCategory) {
            Category.EXERCISE -> Intent(this, ChallengePersonalMyExercisePageActivity::class.java)
            Category.QUIT_ALCOHOL -> Intent(this, ChallengePersonalMyNoDrinkPageActivity::class.java)
            Category.QUIT_SMOKE -> Intent(this, ChallengePersonalMyNoSmokePageActivity::class.java)
            else -> null
        }?.apply { putExtra("challengeId", challengeId) }

        intent?.let { startActivity(it) } ?: showToast("올바른 카테고리가 아닙니다.")
    }

    private fun getAccessToken(context: Context): String? {
        return context.getSharedPreferences("saved_user_info", Context.MODE_PRIVATE)
            .getString("accessToken", null)
    }

    private fun saveChallengeIdToPreferences(category: Category, challengeId: Long) {
        getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
            .putLong("challengeId_${category.name}", challengeId)
            .apply()
        Log.d("ChallengeCreateOrSearch", "챌린지 ID 저장: 카테고리=${category.name}, ID=$challengeId")
    }

    private fun getChallengeIdFromPreferences(category: Category): Long {
        return getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getLong("challengeId_${category.name}", DEFAULT_CHALLENGE_ID)
    }

    private fun isValidChallengeId(challengeId: Long): Boolean {
        return challengeId != -1L && challengeId != 0L
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val PREF_NAME = "ChallengePreferences"
        private const val DEFAULT_CHALLENGE_ID = -1L
    }
}
