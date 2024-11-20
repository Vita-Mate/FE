package com.my.vitamateapp.Challenge

import TeamRankAdapter
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.my.vitamateapp.Api.GetExerciseRankingApi
import com.my.vitamateapp.Api.RetrofitInstance
import com.my.vitamateapp.ChallengeDTO.Category
import com.my.vitamateapp.ChallengeDTO.ListGetExerciseRankingResponse
import com.my.vitamateapp.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OXTeamRank : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TeamRankAdapter
    private var selectedCategory: Category? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 레이아웃 inflate 및 반환
        return inflater.inflate(R.layout.fragment_ox_team_rank, container, false)
    }
}

//        // RecyclerView 설정
//        recyclerView = view.findViewById(R.id.team_rank_recycler_view)
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//
//        // 전달받은 카테고리를 selectedCategory에 할당
//        val categoryString = arguments?.getString("category")
//        selectedCategory = categoryString?.let { Category.valueOf(it) }
//
//        // 데이터 가져오기
//        fetchRankingData()
//
//        return view
//    }
//
//
//    private fun fetchRankingData() {
//        val api = RetrofitInstance.getInstance().create(GetExerciseRankingApi::class.java)
//
//        val accessToken = getAccessToken(requireContext()) ?: run {
//            Log.w(TAG, "Access Token이 null입니다.")
//            return
//        }
//
//        val challengeId = arguments?.getLong("challengeId")
//        challengeId?.let {
//            api.getExerciseRanking("Bearer $accessToken", selectedCategory?.name ?: "").enqueue(object : Callback<ListGetExerciseRankingResponse> {
//                override fun onResponse(
//                    call: Call<ListGetExerciseRankingResponse>,
//                    response: Response<ListGetExerciseRankingResponse>
//                ) {
//                    if (response.isSuccessful && response.body()?.isSuccess == true) {
//                        val rankingResult = response.body()?.result
//
//                        // List<TeamRankItem>로 데이터를 어댑터에 맞게 변환
//                        val rankingData = rankingResult ?: emptyList()
//
//                        // 어댑터에 데이터 설정
//                        adapter = TeamRankAdapter(rankingData)
//                        recyclerView.adapter = adapter
//                    } else {
//                        Log.e(TAG, "API 호출 실패: ${response.message()}")
//                    }
//                }
//
//                override fun onFailure(call: Call<ListGetExerciseRankingResponse>, t: Throwable) {
//                    Log.e(TAG, "API 호출 실패: ${t.message}")
//                }
//            })
//        } ?: run {
//            Log.e(TAG, "Challenge ID가 유효하지 않습니다.")
//        }
//    }
//
//    companion object {
//        fun newInstance(challengeId: Long, category: String): OXTeamRank {
//            return OXTeamRank().apply {
//                arguments = Bundle().apply {
//                    putLong("challengeId", challengeId)
//                    putString("category", category)
//                }
//            }
//        }
//    }
//    private fun getAccessToken(context: Context): String? {
//        val sharedPref = context.getSharedPreferences("saved_user_info", Context.MODE_PRIVATE)
//        return sharedPref.getString("accessToken", null)
//    }
