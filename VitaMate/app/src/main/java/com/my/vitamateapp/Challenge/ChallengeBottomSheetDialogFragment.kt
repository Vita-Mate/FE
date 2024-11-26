package com.my.vitamateapp.Challenge

import AddExerciseRecordApi
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Event.START_DATE
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kakao.sdk.friend.m.t
import com.my.vitamateapp.Api.RetrofitInstance
import com.my.vitamateapp.ChallengeDTO.AddExerciseRecordResponse
import com.my.vitamateapp.ChallengeDTO.AddExerciseResponseRequest
import com.my.vitamateapp.ChallengeDTO.Intensity
import com.my.vitamateapp.HomeActivity
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.FragmentChallengeBottomSheetDialogBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChallengeBottomSheetDialogFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentChallengeBottomSheetDialogBinding? = null
    private val binding get() = _binding!!
    private var challengeId: Long? = null  // challengeId를 멤버 변수로 추가

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChallengeBottomSheetDialogBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.userImageView.setOnClickListener {
            uploadPicture()
        }

        // 버튼 클릭 리스너 설정
        binding.enrollExerciseSaveButton.setOnClickListener {
            saveInformation() // ChallengeBottomSheetDialog2Fragment로 이동
        }

        setupButtonListeners()
    }


    private fun uploadPicture() {

        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_challenge_image_view, null)
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)

        val alertDialog = dialogBuilder.create()

        // Set up button actions
        val buttonCamera = dialogView.findViewById<ImageButton>(R.id.buttonCamera)

        buttonCamera.setOnClickListener {
            // Handle camera button click
            alertDialog.dismiss()
        }

        alertDialog.show()
    }



    // ChallengeBottomSheetDialog2Fragment로 데이터를 전달하는 함수
    private fun saveInformation() {

        val challengeId = getChallengeIdByCategory(context, "EXERCISE")

        // challengeId 값 확인
        if (challengeId != -1L) {
            Log.d("Challenge", "Saved challengeId: $challengeId")
        } else {
            Log.d("Challenge", "No challengeId found for category.")
        }

        // 운동 이름
        val exerciseName = binding.exerciseName.text.toString()

        // 운동 강도 선택
        val exerciseIntensity = when {
            binding.exerciseVeryEasy.isSelected -> Intensity.VERY_EASY
            binding.exerciseEasy.isSelected -> Intensity.EASY
            binding.exerciseLittleStrong.isSelected -> Intensity.MODERATE
            binding.exerciseStrong.isSelected -> Intensity.VERY_HARD
            binding.exerciseVeryStrong.isSelected -> Intensity.EXTREME
            else -> Intensity.EASY // 기본 값 설정
        }

        val startHour = binding.startHour.text.toString().toIntOrNull() ?: 0
        val startMinute = binding.startMinute.text.toString().toIntOrNull() ?: 0
        val endHour = binding.endHour.text.toString().toIntOrNull() ?: 0
        val endMinute = binding.endMinute.text.toString().toIntOrNull() ?: 0
        val comment = binding.recordExerciseMemo.text.toString()

        val request = AddExerciseResponseRequest(
            exerciseType = exerciseName,
            Intensity = exerciseIntensity,
            startHour = startHour,
            startMinute = startMinute,
            endHour = endHour,
            endMinute = endMinute,
            comment = comment
        )


        val accessToken = getAccessToken(requireContext()) ?: run {
            showToast("Access Token이 없습니다.")
            return
        }


        val api = RetrofitInstance.getInstance().create(AddExerciseRecordApi::class.java)

        api.addExerciseRecord("Bearer $accessToken", challengeId, request).enqueue(object : Callback<AddExerciseRecordResponse> {
            override fun onResponse(
                call: Call<AddExerciseRecordResponse>,
                response: Response<AddExerciseRecordResponse>
            ) {
                if (response.isSuccessful == true) {
                    showToast("기록되었습니다")
                    Log.d("ChallengeBottomSheetDialogFragment", "AddExercise Successful: ${response.body()}")

                    // 성공적으로 저장되었으면 마이페이지 화면으로 이동
                    val intent = Intent(requireContext(), ChallengeMyExercisePageActivity::class.java).apply {
                        putExtra("challengeId", challengeId) // challengeId 전달
                    }
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "운동 기록 등록에 실패했습니다."
                    Log.e("ChallengeBottomSheetDialogFragment", "Error Response Code: ${response.code()}")
                    Log.e("ChallengeBottomSheetDialogFragment", "Error Body: $errorMessage")
                    showToast(errorMessage)
                }
            }

            override fun onFailure(call: Call<AddExerciseRecordResponse>, t: Throwable) {
                Log.e("ChallengeBottomSheetDialogFragment", "API 호출 실패: ${t.message}")
                showToast("API 호출 실패: ${t.message}")
            }
        })


//        // ChallengeBottomSheetDialog2Fragment로 데이터 전달
//        val fragment = ChallengeBottomSheetDialog2Fragment().apply {
//            arguments = bundle
//        }

        // 여기서 API 호출 후 성공 시
        showToast("기록되었습니다")
    }

    private fun getChallengeIdByCategory(context: Context?, category: String): Long {
        return context?.let {
            val sharedPref = context.getSharedPreferences("ChallengePrefs", Context.MODE_PRIVATE)
            val challengeId = sharedPref.getLong("challengeId_$category", -1L)
            Log.d("Challenge", "Retrieved challengeId for category $category: $challengeId")
            return challengeId
        } ?: -1L
    }

//    private fun saveRecordIdByCategory(context: Context, category: String, recordId: Long) {
//        val sharedPref = context.getSharedPreferences("saved_user_info", Context.MODE_PRIVATE)
//        val editor = sharedPref.edit()
//
//        // 카테고리별로 recordId를 저장
//        editor.putString("recordId_$category", recordId.toString())
//        editor.apply()
//    }

    // Initialize button click listeners
    private fun setupButtonListeners() {
        binding.exerciseVeryEasy.setOnClickListener { selectIntensityButton(binding.exerciseVeryEasy) }
        binding.exerciseEasy.setOnClickListener { selectIntensityButton(binding.exerciseEasy) }
        binding.exerciseLittleStrong.setOnClickListener { selectIntensityButton(binding.exerciseLittleStrong) }
        binding.exerciseStrong.setOnClickListener { selectIntensityButton(binding.exerciseStrong) }
        binding.exerciseVeryStrong.setOnClickListener { selectIntensityButton(binding.exerciseVeryStrong) }
    }

    // Handle button selection and update UI
    private fun selectIntensityButton(selectedButton: View) {
        // Deselect all buttons
        listOf(
            binding.exerciseVeryEasy,
            binding.exerciseEasy,
            binding.exerciseLittleStrong,
            binding.exerciseStrong,
            binding.exerciseVeryStrong
        ).forEach {
            it.isSelected = it == selectedButton
            // Optionally update button background color or drawable here
            it.setBackgroundResource(if (it.isSelected) R.drawable.button_selected_background else R.drawable.button_default_background)
        }
    }


    private fun getAccessToken(context: Context): String? {
        val sharedPref = context.getSharedPreferences("saved_user_info", Context.MODE_PRIVATE)
        return sharedPref.getString("accessToken", null)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}