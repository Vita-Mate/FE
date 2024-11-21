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
            Log.e("FragmentOXMyRecord", "Invalid challengeId")
        }

        // UI 초기화
        initializeUI()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 전달받은 challengeId 확인
        Log.d("FragmentOXMyRecord", "Received challengeId in onViewCreated: $challengeId")
    }

    /**
     * UI 초기화 함수
     */
    private fun initializeUI() {
        resetButtonBackground() // 버튼 배경 초기화
        setButtonListeners() // 버튼 리스너 설정
    }

    /**
     * 버튼 배경 초기화
     */
    private fun resetButtonBackground() {
        val isOSelected = sharedPreferences.getBoolean("buttonOSelected", true) // 기본값은 O 선택
        updateButtonBackground(isOSelected)
    }

    /**
     * 버튼 클릭 리스너 설정
     */
    private fun setButtonListeners() {
        binding.buttonO.setOnClickListener { handleSelection(true) }
        binding.buttonX.setOnClickListener { handleSelection(false) }
    }

    /**
     * O 또는 X 버튼 클릭 처리
     * @param isOSelected O 버튼이 선택되었는지 여부
     */
    private fun handleSelection(isOSelected: Boolean) {
        updateButtonBackground(isOSelected)
        saveSelectionToPreferences(isOSelected)
        recordChallengeStatus(isOSelected)
    }

    /**
     * 선택한 버튼 상태를 SharedPreferences에 저장
     */
    private fun saveSelectionToPreferences(isOSelected: Boolean) {
        sharedPreferences.edit()
            .putBoolean("buttonOSelected", isOSelected)
            .apply()
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
     * 서버에 챌린지 상태 기록
     * @param isOSelected O 버튼이 선택되었는지 여부
     */
    private fun recordChallengeStatus(isOSelected: Boolean) {
        if (challengeId == null) {
            showToast("챌린지 ID가 유효하지 않습니다.")
            Log.e("FragmentOXMyRecord", "Invalid challengeId")
            return
        }

        val accessToken = getAccessToken() ?: run {
            Log.e("FragmentOXMyRecord", "Access Token is missing.")
            showToast("Access Token이 없습니다.")
            return
        }

        Log.d("FragmentOXMyRecord", "Making API call with challengeId: $challengeId and status: $isOSelected")

        val apiService = RetrofitInstance.getInstance().create(AddQuitChallengeRecordApi::class.java)
        apiService.updateChallengeStatus("Bearer $accessToken", challengeId!!, isOSelected)
            .enqueue(object : Callback<AddQuitChallengeResponse> {
                override fun onResponse(call: Call<AddQuitChallengeResponse>, response: Response<AddQuitChallengeResponse>) {
                    if (response.isSuccessful) {
                        Log.d("FragmentOXMyRecord", "Challenge status updated successfully.")
                        showToast("상태가 성공적으로 저장되었습니다.")
                    } else {
                        val errorBody = response.errorBody()?.string() ?: "Unknown error"
                        Log.e("FragmentOXMyRecord", "API Error: $errorBody")
                        showToast("서버 에러 발생: $errorBody")
                    }
                }

                override fun onFailure(call: Call<AddQuitChallengeResponse>, t: Throwable) {
                    val errorMessage = t.localizedMessage ?: "알 수 없는 네트워크 에러"
                    Log.e("FragmentOXMyRecord", "Network error: $errorMessage")
                    showToast("네트워크 에러: $errorMessage")
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
