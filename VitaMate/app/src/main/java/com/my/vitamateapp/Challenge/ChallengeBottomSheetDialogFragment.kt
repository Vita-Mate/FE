package com.my.vitamateapp.Challenge

import AddExerciseRecordApi
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.my.vitamateapp.Api.RetrofitInstance
import com.my.vitamateapp.ChallengeDTO.AddExerciseRecordResponse
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.FragmentChallengeBottomSheetDialogBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ChallengeBottomSheetDialogFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentChallengeBottomSheetDialogBinding? = null
    private val binding get() = _binding!!
    private var challengeId: Long? = null  // challengeId를 멤버 변수로 추가

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChallengeBottomSheetDialogBinding.inflate(inflater, container, false)

        // arguments에서 challengeId를 받음
        challengeId = arguments?.getLong("challengeId", -1L)
        Log.d("ChallengeBottomSheetDialogFragment", "Received challengeId: $challengeId")

        // Other code for initialization...
        return inflater.inflate(R.layout.fragment_challenge_bottom_sheet_dialog, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.userImageView.setOnClickListener {
//            goSelectImage() // 눌렀을때 ChallengeImageViewFragment로 가게 하기
//        }

        binding.enrollExerciseSaveButton.setOnClickListener {
            saveInformation() // ChallengeBottomSheetDialog2Fragment로 이동
        }

        setupButtonListeners()
    }

//    private fun goSelectImage() {
//        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_challenge_image_view, null)
//        val dialogBuilder = AlertDialog.Builder(requireContext())
//            .setView(dialogView)
//            .setCancelable(true)
//
//        val alertDialog = dialogBuilder.create()
//
//        val buttonCamera = dialogView.findViewById<ImageButton>(R.id.buttonCamera)
//
//        buttonCamera.setOnClickListener {
//            alertDialog.dismiss()
//        }
//
//        alertDialog.show()
//    }


    private fun saveInformation() {

        val challengeId = getChallengeIdByCategory(context, "exercise")

// challengeId 값 확인
        if (challengeId != -1L) {
            Log.d("Challenge", "Saved challengeId: $challengeId")
        } else {
            Log.d("Challenge", "No challengeId found for category.")
        }

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
            showToast("Access Token이 없습니다.")
            return
        }


        val api: AddExerciseRecordApi = RetrofitInstance.getInstance().create(AddExerciseRecordApi::class.java)

        api.addExerciseRecord("Bearer $accessToken", challengeId).enqueue(object : Callback<AddExerciseRecordResponse> {
            override fun onResponse(call: Call<AddExerciseRecordResponse>, response: Response<AddExerciseRecordResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.isSuccess == false) {
                        val recordId = responseBody.result?.recordId
                        if (recordId != null) {
                            saveRecordIdByCategory(requireContext(), "exercise", recordId)

                            // 화면 이동
                            val fragment = ChallengeBottomSheetDialog2Fragment().apply {
                                arguments = Bundle().apply {
                                    putString("exerciseName", exerciseName)
                                    putString("exerciseIntensity", exerciseIntensity)
                                    putString("exerciseTime", exerciseTime)
                                    putString("memo", memo)
                                }
                            }
                            fragment.show(requireActivity().supportFragmentManager, fragment.tag)
                        } else {
                            Log.e("ChallengeBottomSheet", "Record ID is null")
                            showToast("운동 기록 ID를 받아올 수 없습니다.")
                        }
                    } else {
                        val errorMessage = responseBody?.message ?: "운동 기록 등록에 실패했습니다."
                        Log.e("ChallengeBottomSheet", "Error Message: $errorMessage")
                        showToast(errorMessage)
                    }
                } else {
                    val errorMessage = response.errorBody()?.string()
                    Log.e("ChallengeBottomSheet", "Error Response: $errorMessage")
                    showToast(errorMessage ?: "운동 기록 등록에 실패했습니다.")
                }
            }

            override fun onFailure(call: Call<AddExerciseRecordResponse>, t: Throwable) {
                Log.e("ChallengeBottomSheet", "API 호출 실패: ${t.message}")
                showToast("API 호출 실패: ${t.message}")
            }
        })
    }


    private fun getChallengeIdByCategory(context: Context?, category: String): Long {
        return context?.let {
        val sharedPref = context.getSharedPreferences("ChallengePreferences", Context.MODE_PRIVATE)
        val challengeId = sharedPref.getLong("challengeId_$category", -1L)
        Log.d("Challenge", "Retrieved challengeId for category $category: $challengeId")
        return challengeId
        } ?: -1L
    }


    private fun saveRecordIdByCategory(context: Context, category: String, recordId: Long) {
        val sharedPref = context.getSharedPreferences("saved_user_info", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        // 카테고리별로 recordId를 저장
        editor.putString("recordId_$category", recordId.toString())
        editor.apply()
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
