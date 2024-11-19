package com.my.vitamateapp.Challenge

import AddQuitChallengeRecordApi
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.my.vitamateapp.Api.RetrofitInstance
import com.my.vitamateapp.ChallengeDTO.AddQuitChallengeResponse
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.FragmentOxRecordBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentOXMyRecord : Fragment() {
    private lateinit var binding: FragmentOxRecordBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var challengeId: Long? = null

    companion object {
        private const val CHALLENGE_PREFERENCES_KEY = "ChallengePreferences"
        private const val SAVED_USER_INFO_KEY = "saved_user_info"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 데이터 바인딩 초기화
        binding = FragmentOxRecordBinding.inflate(inflater, container, false)
        sharedPreferences = requireContext().getSharedPreferences(CHALLENGE_PREFERENCES_KEY, Context.MODE_PRIVATE)

        // arguments에서 challengeId 가져오기
        challengeId = arguments?.getLong("challengeId")?.takeIf { it != -1L }
        if (challengeId == null) {
            showToast("유효하지 않은 챌린지 ID입니다.")
            Log.e("FragmentOXMyRecord", "Invalid challengeId")
        }

        // UI 초기화
        initializeUI()
        return binding.root
    }

    /**
     * UI 초기화 함수
     */
    private fun initializeUI() {
        setButtonListeners() // 버튼 리스너 설정
        resetButtonBackground() // 버튼 배경 초기화
    }

    /**
     * 버튼 클릭 리스너 설정
     */
    private fun setButtonListeners() {
        binding.buttonO.setOnClickListener {
            handleSelection(isOSelected = true)
        }
        binding.buttonX.setOnClickListener {
            handleSelection(isOSelected = false)
        }
    }

    /**
     * O 또는 X 버튼 클릭 처리
     * @param isOSelected O 버튼이 선택되었는지 여부
     */
    private fun handleSelection(isOSelected: Boolean) {
        updateButtonBackground(isOSelected) // 버튼 배경 업데이트
        recordChallengeStatus(isOSelected) // 서버에 상태 기록
    }

    /**
     * 버튼 배경 업데이트
     * @param isOSelected O 버튼이 선택되었는지 여부
     */
    private fun updateButtonBackground(isOSelected: Boolean) {
        val selectedBackground = R.drawable.button_selected_background
        val defaultBackground = R.drawable.button_default_background

        // 선택된 버튼 배경 업데이트
        binding.buttonO.background = ContextCompat.getDrawable(
            requireContext(),
            if (isOSelected) selectedBackground else defaultBackground
        )
        binding.buttonX.background = ContextCompat.getDrawable(
            requireContext(),
            if (isOSelected) defaultBackground else selectedBackground
        )
    }

    /**
     * 버튼 배경 초기화
     */
    private fun resetButtonBackground() {
        val defaultBackground = R.drawable.button_default_background
        binding.buttonO.background = ContextCompat.getDrawable(requireContext(), defaultBackground)
        binding.buttonX.background = ContextCompat.getDrawable(requireContext(), defaultBackground)
    }

    /**
     * 서버에 챌린지 상태 기록
     * @param isOSelected O 버튼이 선택되었는지 여부
     */
    private fun recordChallengeStatus(isOSelected: Boolean) {
        if (challengeId == null) {
            showToast("유효하지 않은 챌린지 ID입니다.")
            Log.e("FragmentOXMyRecord", "Invalid challengeId")
            return
        }

        val accessToken = getAccessToken() ?: run {
            showToast("Access Token이 없습니다.")
            return
        }

        val status = if (isOSelected) "O" else "X"
        val apiService = RetrofitInstance.getInstance().create(AddQuitChallengeRecordApi::class.java)

        apiService.updateChallengeStatus("Bearer $accessToken", challengeId!!, status)
            .enqueue(object : Callback<AddQuitChallengeResponse> {
                override fun onResponse(call: Call<AddQuitChallengeResponse>, response: Response<AddQuitChallengeResponse>) {
                    if (response.isSuccessful == false) {
                        showToast("상태가 성공적으로 저장되었습니다.")
                    } else {
                        val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                        Log.e("FragmentOXMyRecord", "Error: $errorMessage")
                        showToast("서버 에러 발생: $errorMessage")
                    }
                }

                override fun onFailure(call: Call<AddQuitChallengeResponse>, t: Throwable) {
                    Log.e("FragmentOXMyRecord", "Network error: ${t.localizedMessage}")
                    showToast("네트워크 에러: ${t.localizedMessage}")
                }
            })
    }

    /**
     * SharedPreferences에서 Access Token 가져오기
     */
    private fun getAccessToken(): String? {
        val sharedPref = requireContext().getSharedPreferences(SAVED_USER_INFO_KEY, Context.MODE_PRIVATE)
        return sharedPref.getString("accessToken", null)
    }

    /**
     * Toast 메시지 표시
     * @param message 표시할 메시지
     */
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
