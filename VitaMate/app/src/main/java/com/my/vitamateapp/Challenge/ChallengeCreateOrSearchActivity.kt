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
    private var selectedCategory: Category? = null  // 선택된 카테고리 저장
    private var challengeId: Long? = null  // challengeId를 멤버 변수로 추가
    private var startDate: String? = null
    private var dday: Int? = null  // dday 값을 저장할 변수

    private val participatingApi: ParticipatingChallengeApi = RetrofitInstance.getInstance().create(
        ParticipatingChallengeApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_challenge_createorsearch)

        // Intent로 전달된 카테고리와 challengeId를 selectedCategory와 challengeId에 할당
        val categoryString = intent.getStringExtra("category")
        selectedCategory = categoryString?.let { Category.valueOf(it) }
        challengeId = intent.getLongExtra("challengeId", -1L)  // challengeId를 Intent에서 받음

        binding.closeBtn.setOnClickListener { finish() }

        // 처음 버튼 상태 업데이트
        updateChallengeButton()

        binding.searchChallenge.setOnClickListener {
            searchChallenge()
        }
    }

    override fun onResume() {
        super.onResume()
        // 액티비티가 다시 표시될 때 버튼 상태 업데이트
        updateChallengeButton()
    }

    private fun updateChallengeButton() {
        if (selectedCategory == null) {
            // 카테고리가 선택되지 않은 경우
            binding.createChallenge.text = "챌린지 만들기"
            binding.createChallenge.setOnClickListener { createChallenge() }
            return
        }

        // 현재 선택된 카테고리의 챌린지 ID 가져오기
        challengeId = getChallengeIdFromPreferences(selectedCategory!!)

        if (challengeId == null || !isValidChallengeId(challengeId!!)) {
            binding.createChallenge.text = "챌린지 만들기"
            binding.createChallenge.setOnClickListener { createChallenge() }
        } else {
            fetchChallengeData()
            binding.createChallenge.text = "마이 페이지"
            binding.createChallenge.setOnClickListener { goToMyPage(challengeId!!) }
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

    private fun createChallenge() {
        val intent = Intent(this, ChallengeCreateGroupActivity::class.java)
        selectedCategory?.let { intent.putExtra("category", it.name) }
        startActivity(intent)
    }

    private fun searchChallenge() {
        val intent = Intent(this, ChallengeSearchGroup::class.java)
        selectedCategory?.let { intent.putExtra("category", it.name) }
        startActivity(intent)
    }


    private fun goToMyPage(challengeId: Long) {
        if (selectedCategory == null) {
            showToast("카테고리가 선택되지 않았습니다.")
            return
        }

        // 현재 날짜와 dday 비교
        if (dday != null && dday!! > 0) { //// 꼭 dday!! >= 0으로 바꾸기!!!!!
            showToast("아직 챌린지가 시작되지 않았습니다.")
            return
        }

        val intent = when (selectedCategory) {
            Category.EXERCISE -> {
                val exerciseIntent = Intent(this, ChallengeMyExercisePageActivity::class.java)
                exerciseIntent.putExtra("challengeId", challengeId) // challengeId 전달
                exerciseIntent
            }

            Category.QUIT_ALCOHOL -> {
                val quitAlcoholIntent = Intent(this, ChallengeMyNoDrinkPageActivity::class.java)
                quitAlcoholIntent.putExtra("challengeId", challengeId)
                quitAlcoholIntent
            }
            Category.QUIT_SMOKE -> {
                val quitSmokeIntent = Intent(this, ChallengeMyNoSmokePageActivity::class.java)
                quitSmokeIntent.putExtra("challengeId", challengeId)
                quitSmokeIntent
            }
            else -> null
        }
        intent?.let {
            startActivity(it) // intent가 null이 아니면 startActivity 호출
        } ?: showToast("올바른 카테고리가 아닙니다.") // intent가 null이면 오류 메시지 표시

    }

    companion object {
        private const val PREF_NAME = "ChallengePreferences"
        private const val DEFAULT_CHALLENGE_ID = -1L
    }

    private fun saveChallengeIdToPreferences(category: Category, challengeId: Long) {
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPref.edit()
            .putLong("challengeId_${category.name}", challengeId)
            .apply()
        Log.d("ChallengeCreateOrSearch", "챌린지 ID 저장: 카테고리=${category.name}, ID=$challengeId")
    }

    private fun getChallengeIdFromPreferences(category: Category): Long {
        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPref.getLong("challengeId_${category.name}", DEFAULT_CHALLENGE_ID)
    }


    // AccessToken을 SharedPreferences에서 가져오기
    private fun getAccessToken(context: Context): String? {
        val sharedPref = context.getSharedPreferences("saved_user_info", Context.MODE_PRIVATE)
        return sharedPref.getString("accessToken", null)
    }

    // Toast 메서드 정의
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // 챌린지 ID가 유효한지 확인하는 메서드
    private fun isValidChallengeId(challengeId: Long): Boolean {
        return challengeId != -1L && challengeId != 0L
    }
}
