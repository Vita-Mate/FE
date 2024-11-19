package com.my.vitamateapp.Challenge

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.FragmentMyExerciseRecordBinding

class FragmentMyExerciseRecord : Fragment(R.layout.fragment_my_exercise_record) {
    private lateinit var binding: FragmentMyExerciseRecordBinding
    private var challengeId: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyExerciseRecordBinding.inflate(inflater, container, false)

        // arguments에서 challengeId를 가져옵니다
        challengeId = arguments?.getLong("challengeId", -1L)  // 기본값 -1L
        Log.d("FragmentMyExerciseRecord", "Received challengeId in onCreateView: $challengeId")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 전달받은 challengeId 확인
        Log.d("FragmentMyExerciseRecord", "Received challengeId in onViewCreated: $challengeId")

        // 버튼 클릭 리스너 설정
        binding.addMyRecord.setOnClickListener {
            Log.d("FragmentMyExerciseRecord", "addMyRecord button clicked")
            showChallengeBottomSheetDialog(challengeId ?: -1L)  // challengeId를 BottomSheetDialogFragment로 전달
        }
    }

    private fun showChallengeBottomSheetDialog(challengeId: Long) {
        val bottomSheetFragment = ChallengeBottomSheetDialogFragment().apply {
            arguments = Bundle().apply {
                putLong("challengeId", challengeId)
            }
        }
        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
    }

    companion object {
        fun newInstance(): FragmentMyExerciseRecord {
            return FragmentMyExerciseRecord()
        }
    }
}
