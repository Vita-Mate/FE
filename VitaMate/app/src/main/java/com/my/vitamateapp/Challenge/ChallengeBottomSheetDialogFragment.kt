package com.my.vitamateapp.Challenge

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.my.vitamateapp.Api.AddExerciseRecordApi
import com.my.vitamateapp.Api.RetrofitInstance
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.FragmentChallengeBottomSheetDialogBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChallengeBottomSheetDialogFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentChallengeBottomSheetDialogBinding? = null
    private val binding get() = _binding!!

    // Retrofit 인터페이스 선언
    private val apiService: AddExerciseRecordApi by lazy {
        RetrofitInstance.getInstance().create(AddExerciseRecordApi::class.java)
    }

    // Challenge ID를 전달받을 인스턴스 메서드
    companion object {
        private const val ARG_CHALLENGE_ID = "challenge_id"

        fun new(challengeId: String): ChallengeBottomSheetDialogFragment {
            val fragment = ChallengeBottomSheetDialogFragment()
            val args = Bundle().apply {
                putString(ARG_CHALLENGE_ID, challengeId)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChallengeBottomSheetDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val challengeId = arguments?.getString(ARG_CHALLENGE_ID) ?: run {
            Log.e("TAG", "Challenge ID가 없습니다.")
            showToast("챌린지 정보가 없습니다.")
            dismiss()
            return
        }

        binding.userImageView.setOnClickListener {
            goSelectImage()
        }

        binding.enrollExerciseSaveButton.setOnClickListener {
            saveInformation(challengeId)
        }

        setupButtonListeners()
    }

    private fun goSelectImage() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_challenge_image_view, null)
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)

        val alertDialog = dialogBuilder.create()

        val buttonCamera = dialogView.findViewById<ImageButton>(R.id.buttonCamera)
        buttonCamera.setOnClickListener {
            // 카메라 버튼 클릭 시 카메라 호출 로직 추가 예정
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun saveInformation(challengeId: String) {
        val exerciseName = binding.exerciseName.text.toString()
        val exerciseIntensity = when {
            binding.exerciseVeryEasy.isSelected -> "매우 가벼움"
            binding.exerciseEasy.isSelected -> "가벼움"
            binding.exerciseLittleStrong.isSelected -> "약간 힘듦"
            binding.exerciseStrong.isSelected -> "힘듦"
            binding.exerciseVeryStrong.isSelected -> "매우 힘듦"
            else -> "미정"
        }
        val exerciseTime = binding.exerciseTime.text.toString()
        val memo = binding.recordExerciseMemo.text.toString()

        val accessToken = getAccessToken(requireContext()) ?: run {
            Log.w("TAG", "Access Token이 null입니다.")
            showToast("로그인 후 다시 시도하세요.")
            return
        }

        // Challenge ID와 Access Token 확인 로그 추가
        Log.d("TAG", "Challenge ID: $challengeId")
        Log.d("TAG", "Access Token: $accessToken")

        apiService.addExerciseRecord(challengeId, accessToken).enqueue(object : Callback<AddExerciseRecordResponse> {
            override fun onResponse(
                call: Call<AddExerciseRecordResponse>,
                response: Response<AddExerciseRecordResponse>
            ) {
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    showToast("운동 기록이 저장되었습니다.")
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "알 수 없는 오류"
                    showToast("저장 실패: $errorMessage")
                }
            }

            override fun onFailure(call: Call<AddExerciseRecordResponse>, t: Throwable) {
                showToast("네트워크 오류: ${t.message}")
            }
        })
    }

    private fun setupButtonListeners() {
        binding.exerciseVeryEasy.setOnClickListener { selectIntensityButton(binding.exerciseVeryEasy) }
        binding.exerciseEasy.setOnClickListener { selectIntensityButton(binding.exerciseEasy) }
        binding.exerciseLittleStrong.setOnClickListener { selectIntensityButton(binding.exerciseLittleStrong) }
        binding.exerciseStrong.setOnClickListener { selectIntensityButton(binding.exerciseStrong) }
        binding.exerciseVeryStrong.setOnClickListener { selectIntensityButton(binding.exerciseVeryStrong) }
    }

    private fun selectIntensityButton(selectedButton: View) {
        listOf(
            binding.exerciseVeryEasy,
            binding.exerciseEasy,
            binding.exerciseLittleStrong,
            binding.exerciseStrong,
            binding.exerciseVeryStrong
        ).forEach {
            it.isSelected = it == selectedButton
            it.setBackgroundResource(if (it.isSelected) R.drawable.button_selected_background else R.drawable.button_default_background)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getAccessToken(context: Context): String? {
        val sharedPref = context.getSharedPreferences("saved_user_info", Context.MODE_PRIVATE)
        return sharedPref.getString("accessToken", null)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
