package com.my.vitamateapp.Challenge

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.my.vitamateapp.Api.RetrofitInstance
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.ActivityChallengeCreateorsearchBinding
import com.my.vitamateapp.network.ChallengeJoinResultApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChallengeCreateOrSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChallengeCreateorsearchBinding
    private var selectedCategory: Category? = null  // 선택된 카테고리 저장
    private var challengeId: Long? = null  // challengeId를 멤버 변수로 추가

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
        // challengeId가 null이거나 -1L이면 "챌린지 검색" 버튼을 표시
        if (challengeId == null || challengeId == -1L || !isValidChallengeId(challengeId!!)) {
            binding.createChallenge.text = "챌린지 만들기"
            binding.createChallenge.setOnClickListener {
                createChallenge()
            }
        } else {
            // challengeId가 유효하면 "마이 페이지" 버튼을 표시
            binding.createChallenge.text = "마이 페이지"
            binding.createChallenge.setOnClickListener {
                // 유효한 challengeId일 때만 마이 페이지로 이동
                goToMyPage(challengeId!!)
            }
        }
    }
    private fun createChallenge() {
        val intent = Intent(this, ChallengeCreateGroupActivity::class.java).apply {
            putExtra("category", selectedCategory?.name)
        }
        startActivity(intent)
    }


    // 챌린지 검색 페이지로 이동
    private fun searchChallenge() {
        val intent = Intent(this, ChallengeSearchGroup::class.java).apply {
            putExtra("category", selectedCategory?.name)
        }
        startActivity(intent)
    }

    // 참여한 챌린지의 카테고리에 따라 마이 페이지로 이동
    private fun goToMyPage(challengeId: Long) {
        val intent = when (selectedCategory) {
            Category.EXERCISE -> Intent(this, ChallengeMyExercisePageActivity::class.java)
            Category.QUIT_ALCOHOL -> Intent(this, ChallengeMyNoDrinkPageActivity::class.java)
            Category.QUIT_SMOKE -> Intent(this, ChallengeMyNoSmokePageActivity::class.java)
            else -> null
        }
        intent?.let {
            it.putExtra("challengeId", challengeId)  // challengeId를 전달
            startActivity(it)
        }
    }

    // SharedPreferences에 챌린지 ID 저장
    private fun saveChallengeIdToPreferences(challengeId: Long) {
        val sharedPref = getSharedPreferences("ChallengePreferences", Context.MODE_PRIVATE)
        sharedPref.edit().putLong("challengeId", challengeId).apply()
        Log.d("challengeId", "챌린지 ID 저장: $challengeId")
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
