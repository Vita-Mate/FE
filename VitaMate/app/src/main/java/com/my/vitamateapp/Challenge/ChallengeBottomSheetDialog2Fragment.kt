package com.my.vitamateapp.Challenge

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.my.vitamateapp.databinding.FragmentChallengeBottomSheetDialog2Binding
import com.my.vitamateapp.databinding.FragmentChallengeBottomSheetDialogBinding

class ChallengeBottomSheetDialog2Fragment : BottomSheetDialogFragment() {

    private var _binding: FragmentChallengeBottomSheetDialog2Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChallengeBottomSheetDialog2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pictureRecordCloseButton.setOnClickListener {
            dismiss() // 창 닫기
        }

        // ImageView 클릭 시 ChallengeBottomSheetDialogFragment 표시하기
        binding.challengeImage1.setOnClickListener { showChallengeBottomSheetDialog() }
        binding.challengeImage2.setOnClickListener { showChallengeBottomSheetDialog() }
        binding.challengeImage3.setOnClickListener { showChallengeBottomSheetDialog() }
        binding.challengeImage4.setOnClickListener { showChallengeBottomSheetDialog() }
        binding.challengeImage5.setOnClickListener { showChallengeBottomSheetDialog() }
        binding.challengeImage6.setOnClickListener { showChallengeBottomSheetDialog() }
    }

    private fun showChallengeBottomSheetDialog() {
        val dialogFragment = ChallengeBottomSheetDialogFragment()
        dialogFragment.show(parentFragmentManager, dialogFragment.tag)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
