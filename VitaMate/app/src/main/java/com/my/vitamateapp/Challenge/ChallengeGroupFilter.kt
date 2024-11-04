package com.my.vitamateapp.Challenge

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.SearchChallGroupBinding
import com.my.vitamateapp.databinding.SearchGroupFilterBinding

class ChallengeGroupFilter : AppCompatActivity() {
    private lateinit var binding: SearchGroupFilterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // DataBindingUtil을 사용하여 레이아웃 파일을 연결
        binding = DataBindingUtil.setContentView(this, R.layout.search_group_filter)

        // NumberPicker를 초기화합니다.
        setupNumberPickers()

        // 버튼의 클릭 리스너를 설정합니다.
        setupButtonListeners()
    }

    // NumberPicker 초기화: 최소값과 최대값 설정
    private fun setupNumberPickers() {
        binding.enPeopleStart.apply {
            minValue = 2 // 시작 인원 최소값
            maxValue = 10 // 시작 인원 최대값
        }

        binding.enPeopleEnd.apply {
            minValue = 2 // 종료 인원 최소값
            maxValue = 10 // 종료 인원 최대값
        }
    }

    // 버튼 클릭 리스너 설정
    private fun setupButtonListeners() {
        // 운동 횟수 버튼 클릭 리스너 설정
        binding.countExercise1.setOnClickListener {
            // 주 1회 버튼이 클릭되었을 때 처리할 로직
            Toast.makeText(this, "운동 횟수: 주 1회 선택", Toast.LENGTH_SHORT).show()
        }

        binding.countExercise2.setOnClickListener {
            // 주 2회 버튼이 클릭되었을 때 처리할 로직
            Toast.makeText(this, "운동 횟수: 주 2회 선택", Toast.LENGTH_SHORT).show()
        }

        // 추가 운동 횟수 버튼에 대한 클릭 리스너 설정
        // 예제:
        binding.countExercise3.setOnClickListener {
            Toast.makeText(this, "운동 횟수: 주 3회 선택", Toast.LENGTH_SHORT).show()
        }
        binding.countExercise4.setOnClickListener {
            Toast.makeText(this, "운동 횟수: 주 4회 선택", Toast.LENGTH_SHORT).show()
        }

        // 챌린지 기간 버튼 클릭 리스너 설정
        binding.periodExercise7.setOnClickListener {
            // 1주일 버튼이 클릭되었을 때 처리할 로직
            Toast.makeText(this, "챌린지 기간: 1주일 선택", Toast.LENGTH_SHORT).show()
        }

        binding.periodExercise30.setOnClickListener {
            // 1개월 버튼이 클릭되었을 때 처리할 로직
            Toast.makeText(this, "챌린지 기간: 1개월 선택", Toast.LENGTH_SHORT).show()
        }

        binding.periodExercise90.setOnClickListener {
            // 3개월 버튼이 클릭되었을 때 처리할 로직
            Toast.makeText(this, "챌린지 기간: 3개월 선택", Toast.LENGTH_SHORT).show()
        }

        binding.periodExercise180.setOnClickListener {
            // 6개월 버튼이 클릭되었을 때 처리할 로직
            Toast.makeText(this, "챌린지 기간: 6개월 선택", Toast.LENGTH_SHORT).show()
        }

        binding.periodExercise365.setOnClickListener {
            // 1년 버튼이 클릭되었을 때 처리할 로직
            Toast.makeText(this, "챌린지 기간: 1년 선택", Toast.LENGTH_SHORT).show()
        }

        // 새로고침 버튼 클릭 리스너 설정
        binding.filterRefreshButton.setOnClickListener {
            // 필터 재설정 로직: 모든 선택 항목 초기화
            binding.enPeopleStart.value = 1 // 시작 인원을 초기값으로 설정
            binding.enPeopleEnd.value = 1 // 종료 인원을 초기값으로 설정
            Toast.makeText(this, "필터가 재설정되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}
