package com.my.vitamateapp.Challenge

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.my.vitamateapp.R

class  FragmentOXTeamRecord : Fragment() {

    companion object {
        private const val ARG_CHALLENGE_ID = "challengeId"

        // newInstance 메서드로 데이터 전달
        fun newInstance(challengeId: Long): FragmentOXTeamRecord {
            val fragment = FragmentOXTeamRecord()
            val args = Bundle().apply {
                putLong(ARG_CHALLENGE_ID, challengeId)
            }
            fragment.arguments = args
            return fragment
        }
    }

    private var challengeId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 전달받은 데이터를 저장
        challengeId = arguments?.getLong(ARG_CHALLENGE_ID)
        Log.d("TeamExerciseFragment", "Received Challenge ID: $challengeId")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 레이아웃을 inflate
        val view = inflater.inflate(R.layout.fragment_ox_team_record, container, false)

//        // 전달받은 데이터를 화면에 표시
//        val challengeInfoTextView = view.findViewById<TextView>(R.id.challenge_info)
//        challengeInfoTextView.text = "Challenge ID: $challengeId"

        return view
    }
}
