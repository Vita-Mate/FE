package com.my.vitamateapp.Challenge

import android.content.ContentValues.TAG
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import com.my.vitamateapp.HomeActivity

import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.ActivityChallengeSelectModeBinding

class ChallengeSelectModeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChallengeSelectModeBinding
    private var selectedCategory: Category? = null  // 선택된 카테고리 저장

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_challenge_select_mode)

        // 전달받은 카테고리 이름을 Category Enum으로 변환
        val categoryString = intent.getStringExtra("category")
        Log.d(TAG, "Received category: $categoryString")

        selectedCategory = try {
            categoryString?.let { Category.valueOf(it) }
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, "Invalid category: $categoryString", e)
            null  // 유효하지 않은 카테고리일 경우 null 처리
        }

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

    // 챌린지 참여 여부 확인 메서드 (참여한 챌린지 확인)
    private fun isParticipatingInChallenge(key: String?): Boolean {
        val sharedPreferences = getSharedPreferences("ChallengePreferences", MODE_PRIVATE)
        // sharedPreferences에서 참여 여부를 Boolean 타입으로 가져와 확인
        return sharedPreferences.getBoolean(key, false)
    }

    // 버튼 상태 업데이트 메서드
    private fun updateChallengeButton() {
        val participationKey = "isParticipatingIn${selectedCategory?.name}"
        // 참여 여부 확인
        val isParticipating = isParticipatingInChallenge(participationKey)

        // 참여한 챌린지가 있으면 단체 그룹 생성 페이지로 이동
        binding.modeGroup.setOnClickListener {
            mode_group()
        }
    }

    // 단체 그룹 생성 페이지로 이동하는 함수
    private fun mode_group() {
        val challengeId = getChallengeId()  // SharedPreferences에서 저장된 challengeId 가져오기

        val intent = Intent(this, ChallengeCreateOrSearchActivity::class.java).apply {
            selectedCategory?.let { putExtra("category", it.name) }  // Category 이름 전달
            challengeId?.let { putExtra("challengeId", it) }
            Log.d(TAG, "challengeId: $challengeId, Category: ${selectedCategory?.name}")
        }
        startActivity(intent)
    }

    private fun mode_ind() {
        // 새로운 챌린지를 생성하는 페이지로 이동
        val intent = Intent(this, ChallengeCreateIndividualActivity::class.java).apply {
            selectedCategory?.let { putExtra("category", it.name) }  // Category 이름 전달
        }
        startActivity(intent)
    }

    // 닫기 버튼 클릭 시 현재 액티비티 종료하는 함수
    private fun mode_select_close() {
        val intent = Intent(this, HomeActivity::class.java) // 현재 액티비티를 종료하고 홈화면으로 돌아감
        startActivity(intent)
    }

    // challengeId를 SharedPreferences에서 가져오는 메서드
    private fun getChallengeId(): Long? {
        val sharedPreferences = getSharedPreferences("ChallengePreferences", MODE_PRIVATE)
        return sharedPreferences.getLong("challengeId", -1L).takeIf { it != -1L }  // challengeId가 -1L일 경우 null 반환
    }
}
