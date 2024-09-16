package com.my.vitamateapp.Challenge

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.FragmentChallengeBottomSheetDialogBinding

class ChallengeBottomSheetDialogFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentChallengeBottomSheetDialogBinding? = null
    private val binding get() = _binding!!

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
            goSelectImage() // 눌렀을때 ChallengeImageViewFragment로 가게 하기
        }
        // 버튼 클릭 리스너 설정
        binding.enrollExerciseSaveButton.setOnClickListener {
            saveInformation() // ChallengeBottomSheetDialog2Fragment로 이동
        }

        setupButtonListeners()
    }

    private fun goSelectImage() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_challenge_image_view, null)
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)

        val alertDialog = dialogBuilder.create()

        // Set up button actions
        val buttonCamera = dialogView.findViewById<ImageButton>(R.id.buttonCamera)
        val buttonGallery = dialogView.findViewById<ImageButton>(R.id.buttonGallery)

        buttonCamera.setOnClickListener {
            // Handle camera button click
            alertDialog.dismiss()
        }

        buttonGallery.setOnClickListener {
            // Handle gallery button click
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    // ChallengeBottomSheetDialog2Fragment로 데이터를 전달하는 함수
    private fun saveInformation() {
        // 운동 이름
        val exerciseName = binding.exerciseName.text.toString()

        // 운동 강도 선택
        val exerciseIntensity = when {
            binding.exerciseVeryEasy.isSelected -> "매우 가벼움"
            binding.exerciseEasy.isSelected -> "가벼움"
            binding.exerciseLittleStrong.isSelected -> "약간 힘듦"
            binding.exerciseStrong.isSelected -> "힘듦"
            binding.exerciseVeryStrong.isSelected -> "매우 힘듦"
            else -> "미정"
        }

        // 운동 시간
        val exerciseTime = binding.exerciseTime.text.toString()

        // 메모 (선택적)
        val memo = binding.recordExerciseMemo.text.toString()

        // 데이터를 번들에 저장
        val bundle = Bundle().apply {
            putString("exerciseName", exerciseName)
            putString("exerciseIntensity", exerciseIntensity)
            putString("exerciseTime", exerciseTime)
            putString("memo", memo)
        }

        // ChallengeBottomSheetDialog2Fragment로 데이터 전달
        val fragment = ChallengeBottomSheetDialog2Fragment().apply {
            arguments = bundle
        }

        // 두 번째 BottomSheetDialogFragment 띄우기
        fragment.show(parentFragmentManager, fragment.tag)
    }


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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
