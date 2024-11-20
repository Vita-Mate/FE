package com.my.vitamateapp.Challenge

import ChallengeAdapter
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.my.vitamateapp.Api.ChallengeListApi
import com.my.vitamateapp.Api.ParticipatingChallengeApi
import com.my.vitamateapp.Api.RetrofitInstance
import com.my.vitamateapp.ChallengeDTO.Category
import com.my.vitamateapp.ChallengeDTO.ChallengeListResponse
import com.my.vitamateapp.ChallengeDTO.ChallengePreviewResponse
import com.my.vitamateapp.ChallengeDTO.Participating
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.SearchChallGroupBinding
import com.my.vitamateapp.network.ChallengeJoinResultApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChallengeSearchGroup : AppCompatActivity() {
    private lateinit var binding: SearchChallGroupBinding
    private var selectedCategory: Category? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.search_chall_group)


        // 전달받은 카테고리를 selectedCategory에 할당
        val categoryString = intent.getStringExtra("category")
        selectedCategory = categoryString?.let { Category.valueOf(it) }

        // RecyclerView 설정
        binding.challengeRecyclerView.layoutManager = LinearLayoutManager(this)

        // 챌린지 데이터 가져오기
        fetchChallenges()

        // 버튼 클릭 리스너 설정
        binding.challengeFilter.setOnClickListener { goSearch() }
        binding.preButton1.setOnClickListener { goBack() }
        binding.plusSymbol.setOnClickListener { goCreate() }
        binding.shapeBackground.setOnClickListener { goCreate() }
    }

    private fun goSearch() {
        startActivity(Intent(this, ChallengeGroupFilter::class.java))
    }

    private fun goBack() {
        finish()
    }

    private fun goCreate() {
        val intent = Intent(this, ChallengeCreateGroupActivity::class.java).apply {
            putExtra("category", selectedCategory?.name)
        }
        startActivity(intent)
    }

    private fun fetchChallenges() {
        val api = RetrofitInstance.getInstance().create(ChallengeListApi::class.java)
        val accessToken = getAccessToken(this) ?: run {
            Log.w(TAG, "Access Token이 null입니다.")
            showToast("Access Token이 없습니다. 로그인을 다시 시도하세요.")
            return
        }

        selectedCategory?.let { category ->
            api.challengeList("Bearer $accessToken", category.name).enqueue(object : Callback<ChallengeListResponse> {
                override fun onResponse(call: Call<ChallengeListResponse>, response: Response<ChallengeListResponse>) {
                    if (response.isSuccessful && response.body()?.result != null) {
                        val challengeList = response.body()?.result?.challengeList ?: emptyList()
                        fetchParticipatingChallenges(accessToken, category.name) { participatingChallenge ->
                            // 참여 중인 챌린지가 있을 경우 필터링
                            // 필터링된 리스트를 MutableList로 변환
                            val filteredChallengeList = challengeList.filter { it.challengeId != participatingChallenge?.challengeId }.toMutableList()

                            // 어댑터에 필터링된 데이터 전달
                            val adapter = ChallengeAdapter(
                                participatingChallenge,
                                filteredChallengeList,  // 필터링된 데이터
                                RetrofitInstance.getInstance().create(ChallengeJoinResultApi::class.java),
                                this@ChallengeSearchGroup
                            )

                            // RecyclerView에 어댑터 설정
                            binding.challengeRecyclerView.adapter = adapter
                            binding.challengeRecyclerView.visibility = View.VISIBLE // 데이터가 있을 경우 보이도록 설정
                        }
                    } else {
                        Log.e(TAG, "응답 오류: ${response.code()} ${response.message()}")
                        showToast("데이터를 불러올 수 없습니다. 오류 코드: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ChallengeListResponse>, t: Throwable) {
                    Log.e(TAG, "API 호출 실패: ${t.message}", t)
                    showToast("API 호출 실패: ${t.message}")
                }
            })
        } ?: run {
            showToast("카테고리가 선택되지 않았습니다.")
        }
    }

    private fun fetchParticipatingChallenges(accessToken: String, category: String, callback: (Participating?) -> Unit) {
        val api = RetrofitInstance.getInstance().create(ParticipatingChallengeApi::class.java)
        api.ParticipatingList("Bearer $accessToken", category).enqueue(object : Callback<ChallengePreviewResponse> {
            override fun onResponse(call: Call<ChallengePreviewResponse>, response: Response<ChallengePreviewResponse>) {
                if (response.isSuccessful && response.body()?.isSuccess == false) {
                    // 변경된 result 구조에 맞게 참여 중인 챌린지 가져오기
                    val participatingChallenge = response.body()?.result
                    callback(participatingChallenge)
                } else {
                    Log.e(TAG, "응답 오류: ${response.code()} ${response.message()}")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<ChallengePreviewResponse>, t: Throwable) {
                Log.e(TAG, "API 호출 실패: ${t.message}", t)
                callback(null)
            }
        })
    }


    private fun getAccessToken(context: Context): String? {
        val sharedPref = context.getSharedPreferences("saved_user_info", Context.MODE_PRIVATE)
        return sharedPref.getString("accessToken", null)
    }

    // Toast 메서드 정의
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}