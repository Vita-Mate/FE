package com.my.vitamateapp.mySupplement

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.my.vitamateapp.R
import com.my.vitamateapp.repository.SupplementsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AllReviewsActivity : AppCompatActivity() {
    private lateinit var rvAdapter: ReviewListAdapter
    private lateinit var supplementsRepository: SupplementsRepository
    private val reviews = mutableListOf<ReviewItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_reviews)

        // RecyclerView 설정
        val rv: RecyclerView = findViewById(R.id.all_review_list_rv)
        rv.layoutManager = LinearLayoutManager(this)
        rvAdapter = ReviewListAdapter(reviews)
        rv.adapter = rvAdapter

        // supplementsRepository 초기화
        supplementsRepository = SupplementsRepository(this)

        // API 호출하여 전체 리뷰 불러오기
        loadAllReviews()
    }

    private fun loadAllReviews() {
        // 저장된 영양제 ID 가져오기
        val supplementId = getSavedSupplementId()
        if (supplementId == -1) {
            Log.d("AllReviewsActivity", "유효하지 않은 영양제 ID")
            return
        }

        // SharedPreferences에서 액세스 토큰 가져오기
        val sharedPreferences = getSharedPreferences("saved_user_info", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("accessToken", null)

        if (accessToken.isNullOrEmpty()) {
            Log.e("AllReviewsActivity", "액세스 토큰이 유효하지 않습니다.")
            return
        }

        // 코루틴을 사용하여 API 호출
        CoroutineScope(Dispatchers.IO).launch {
            val result = supplementsRepository.getSupplementReviews(accessToken, supplementId)
            withContext(Dispatchers.Main) {
                if (result != null) {
                    // 불러온 리뷰 데이터를 리스트에 추가
                    reviews.clear()
                    reviews.addAll(result)

                    // 리뷰 리스트를 어댑터에 업데이트
                    rvAdapter.notifyDataSetChanged()

                    // 전체 아이템 표시
                    rvAdapter.expandItems() // 전체 리뷰 표시
                } else {
                    Log.e("AllReviewsActivity", "리뷰 조회 실패")
                }
            }
        }
    }

    // 저장된 영양제 ID를 가져오는 함수
    private fun getSavedSupplementId(): Int {
        val sharedPref = getSharedPreferences("saved_supplement_info", Context.MODE_PRIVATE)
        return sharedPref.getInt("supplementId", -1) // 기본값 -1 (저장된 ID가 없을 때)
    }
}
