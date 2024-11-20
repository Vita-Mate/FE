package com.my.vitamateapp.Challenge

import TeamRankAdapter
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Build.VERSION_CODES.S
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.my.vitamateapp.Api.GetExerciseRankingApi
import com.my.vitamateapp.Api.GetExerciseTeamRecordApi
import com.my.vitamateapp.Api.RetrofitInstance
import com.my.vitamateapp.ChallengeDTO.GetExerciseTeamRecordResponse
import com.my.vitamateapp.ChallengeDTO.GetResult
import com.my.vitamateapp.ChallengeDTO.ListGetExerciseRankingResponse
import com.my.vitamateapp.ChallengeDTO.TeamRankItem
import com.my.vitamateapp.databinding.FragmentTeamExerciseRecordBinding
import com.my.vitamateapp.databinding.FragmentTeamRankBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import java.text.SimpleDateFormat
import java.util.*

class TeamRank : Fragment() {

    private lateinit var binding: FragmentTeamRankBinding
    private lateinit var recyclerViewAdapter: TeamRankAdapter
    private var challengeId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        challengeId = arguments?.getLong("challengeId", -1L)
        Log.d("FragmentTeamExerciseRecord", "Received challengeId in onCreate: $challengeId")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeamRankBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 설정
        val recyclerView = binding.teamRankRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // RecyclerView Adapter 초기화
        recyclerViewAdapter = TeamRankAdapter()
        recyclerView.adapter = recyclerViewAdapter

        // challengeId가 유효한지 확인하고 API 호출
        challengeId?.let {
            fetchTeamRank(it) // Long을 String으로 변환하여 전달
        } ?: run {
            showError("Invalid challengeId")
        }

        val selectedDateString = arguments?.getString("selectedDate") ?: getCurrentDate()
        Log.d("Fragment", "Selected Date: $selectedDateString")
    }

    private fun fetchTeamRank(challengeId: Long) {
        val api = RetrofitInstance.getInstance().create(GetExerciseRankingApi::class.java)
        val accessToken = getAccessToken(requireContext()) ?: run {
            Log.w(TAG, "Access Token이 null입니다.")
            return
        }

        // 날짜를 포함한 API 호출
        val selectedDateString = arguments?.getString("selectedDate") ?: getCurrentDate()

        Log.d(TAG, "Sending request with Token: Bearer $accessToken, challengeId: $challengeId, date: $selectedDateString")

        // 날짜를 포함한 API 호출
        api.getExerciseRanking("Bearer $accessToken", challengeId, selectedDateString).enqueue(object : Callback<ListGetExerciseRankingResponse> {
            override fun onResponse(call: Call<ListGetExerciseRankingResponse>, response: Response<ListGetExerciseRankingResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.isSuccess == false) {
                        updateUI(responseBody.result)
                        Log.d("Success: ", "TeamExerciseRanking : $responseBody")
                    } else {
                        showError(responseBody?.message ?: "Unknown error")
                    }
                } else {
                    val errorBody = response.errorBody()?.string() // 서버의 오류 메시지 확인
                    Log.e(TAG, "Error: $errorBody")
                    showError("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ListGetExerciseRankingResponse>, t: Throwable) {
                showError("Failed to load team record: ${t.message}")
            }
        })
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    private fun updateUI(result: List<TeamRankItem>) {
        // RecyclerView Adapter에 데이터 전달
        recyclerViewAdapter.submitList(result)
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        Log.e("FragmentTeamExerciseRank", message)
    }

    private fun getAccessToken(context: Context): String? {
        val sharedPref = context.getSharedPreferences("saved_user_info", Context.MODE_PRIVATE)
        return sharedPref.getString("accessToken", null)
    }
}
