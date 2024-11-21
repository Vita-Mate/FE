package com.my.vitamateapp.Challenge


import OXTeamRankAdapter
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.my.vitamateapp.Api.GetOXTeamRankingApi
import com.my.vitamateapp.Api.RetrofitInstance
import com.my.vitamateapp.ChallengeDTO.GetOXTeamRankingResponse
import com.my.vitamateapp.databinding.FragmentOxTeamRankBinding
import com.my.vitamateapp.ChallengeDTO.OXTeamRank

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import java.text.SimpleDateFormat
import java.util.*

class OXTeamRank : Fragment() {

    private lateinit var binding: FragmentOxTeamRankBinding
    private lateinit var recyclerViewAdapter: OXTeamRankAdapter
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
        binding = FragmentOxTeamRankBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 설정
        val recyclerView = binding.oxTeamRankRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // RecyclerView Adapter 초기화
        recyclerViewAdapter = OXTeamRankAdapter()
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
        val api = RetrofitInstance.getInstance().create(GetOXTeamRankingApi::class.java)
        val accessToken = getAccessToken(requireContext()) ?: run {
            showError("Access token is missing.")
            return
        }

        val selectedDateString = arguments?.getString("selectedDate") ?: getCurrentDate()

        api.getOXRanking("Bearer $accessToken", challengeId, selectedDateString).enqueue(object : Callback<GetOXTeamRankingResponse> {
            override fun onResponse(call: Call<GetOXTeamRankingResponse>, response: Response<GetOXTeamRankingResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.isSuccess == false) {
                        updateUI(responseBody.result)
                    } else {
                        showError(responseBody?.message ?: "No data available.")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    showError("Server error: ${response.code()} - $errorBody")
                }
            }

            override fun onFailure(call: Call<GetOXTeamRankingResponse>, t: Throwable) {
                showError("Failed to load data: ${t.message}")
            }
        })
    }


    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    private fun updateUI(result: List<OXTeamRank>) {
        // RecyclerView Adapter에 데이터 전달
        recyclerViewAdapter.submitList(result)
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        Log.e("GetOXTeamRankingResponse", message)
    }

    private fun getAccessToken(context: Context): String? {
        val sharedPref = context.getSharedPreferences("saved_user_info", Context.MODE_PRIVATE)
        return sharedPref.getString("accessToken", null)
    }
}
