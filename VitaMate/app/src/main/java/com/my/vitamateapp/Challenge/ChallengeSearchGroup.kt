package com.my.vitamateapp.Challenge

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.my.vitamateapp.Api.ChallengeListApi
import com.my.vitamateapp.Api.DeleteChallengeResponseApi
import com.my.vitamateapp.Api.ParticipatingChallengeApi
import com.my.vitamateapp.Api.RetrofitInstance
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.SearchChallGroupBinding
import com.my.vitamateapp.network.ChallengeJoinResultApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChallengeSearchGroup : AppCompatActivity() {
    private lateinit var binding: SearchChallGroupBinding
    private var selectedCategory: Category? = null
    private var participatingChallenge: Participating? = null // 참여 중인 챌린지 정보
    private lateinit var deleteApi: DeleteChallengeResponseApi
    private lateinit var challengeAdapter: ChallengeAdapter

    companion object {
        private const val ACCESS_TOKEN_KEY = "accessToken"
        private const val SHARED_PREF_NAME = "saved_user_info"
        private const val CHALLENGE_CATEGORY_KEY = "category"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.search_chall_group)

        // Intent에서 카테고리 추출
        selectedCategory = intent.getStringExtra(CHALLENGE_CATEGORY_KEY)?.let {
            try {
                Category.valueOf(it)
            } catch (e: IllegalArgumentException) {
                Log.w(TAG, "잘못된 카테고리 전달: $it")
                null
            }
        }

        deleteApi = RetrofitInstance.getInstance().create(DeleteChallengeResponseApi::class.java)

        // RecyclerView 초기화
        initRecyclerView()

        // 챌린지 데이터 가져오기
        fetchChallenges()

        // 버튼 클릭 이벤트 연결
        setupButtonListeners()
    }

    private fun initRecyclerView() {
        challengeAdapter = ChallengeAdapter(
            RetrofitInstance.getInstance().create(ChallengeJoinResultApi::class.java),
            deleteApi,
            this
        )
        binding.challengeRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ChallengeSearchGroup)
            adapter = challengeAdapter
        }
    }

    private fun setupButtonListeners() {
        binding.challengeFilter.setOnClickListener {
            startActivity(Intent(this, ChallengeGroupFilter::class.java))
        }
        binding.preButton1.setOnClickListener {
            startActivity(Intent(this, ChallengeCreateOrSearchActivity::class.java))
        }
        binding.plusSymbol.setOnClickListener { goToCreateChallenge() }
        binding.shapeBackground.setOnClickListener { goToCreateChallenge() }
    }

    private fun goToCreateChallenge() {
        val intent = Intent(this, ChallengeCreateGroupActivity::class.java).apply {
            putExtra(CHALLENGE_CATEGORY_KEY, selectedCategory?.name)
        }
        startActivity(intent)
    }

    private fun fetchChallenges() {
        val api = RetrofitInstance.getInstance().create(ChallengeListApi::class.java)
        val accessToken = getAccessToken() ?: run {
            showToast("Access Token이 없습니다. 로그인을 다시 시도하세요.")
            return
        }

        selectedCategory?.let { category ->
            api.challengeList("Bearer $accessToken", category.name).enqueue(object : Callback<ChallengeListResponse> {
                override fun onResponse(call: Call<ChallengeListResponse>, response: Response<ChallengeListResponse>) {
                    if (response.isSuccessful) {
                        val challengeList = response.body()?.result?.challengeList.orEmpty()
                        fetchParticipatingChallenges(accessToken, category.name, challengeList)
                    } else {
                        Log.e(TAG, "챌린지 데이터 오류: ${response.code()} ${response.message()}")
                        showToast("데이터를 불러올 수 없습니다.")
                    }
                }

                override fun onFailure(call: Call<ChallengeListResponse>, t: Throwable) {
                    Log.e(TAG, "챌린지 데이터 API 실패: ${t.message}", t)
                    showToast("챌린지 데이터를 불러오는 중 오류가 발생했습니다.")
                }
            })
        } ?: showToast("카테고리가 선택되지 않았습니다.")
    }

    private fun fetchParticipatingChallenges(
        accessToken: String,
        category: String,
        challengeList: List<Challenge>
    ) {
        val api = RetrofitInstance.getInstance().create(ParticipatingChallengeApi::class.java)
        api.ParticipatingList("Bearer $accessToken", category).enqueue(object : Callback<ChallengePreviewResponse> {
            override fun onResponse(call: Call<ChallengePreviewResponse>, response: Response<ChallengePreviewResponse>) {
                if (response.isSuccessful) {
                    participatingChallenge = response.body()?.result
                } else {
                    Log.e(TAG, "참여 중인 챌린지 API 오류: ${response.code()} ${response.message()}")
                }
                updateRecyclerView(challengeList)
            }

            override fun onFailure(call: Call<ChallengePreviewResponse>, t: Throwable) {
                Log.e(TAG, "참여 중인 챌린지 API 실패: ${t.message}", t)
                updateRecyclerView(challengeList)
            }
        })
    }

    private fun updateRecyclerView(challengeList: List<Challenge>) {
        val filteredList = participatingChallenge?.let { pc ->
            challengeList.filter { it.challengeId != pc.challengeId }
        } ?: challengeList

        challengeAdapter.submitList(filteredList)
    }

    fun cancelChallenge(challengeId: Long) {
        val accessToken = getAccessToken() ?: run {
            showToast("Access Token이 없습니다.")
            return
        }

        deleteApi.deleteChallenge("Bearer $accessToken", challengeId).enqueue(object : Callback<DeleteChallenge> {
            override fun onResponse(call: Call<DeleteChallenge>, response: Response<DeleteChallenge>) {
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    showToast("챌린지 참여를 취소했습니다.")
                    participatingChallenge = null
                    fetchChallenges() // 챌린지 데이터 다시 가져오기
                } else {
                    showToast("챌린지 취소 실패: 서버 오류")
                }
            }

            override fun onFailure(call: Call<DeleteChallenge>, t: Throwable) {
                showToast("네트워크 오류: ${t.message}")
            }
        })
    }

    private fun getAccessToken(): String? {
        return getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).getString(ACCESS_TOKEN_KEY, null)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
