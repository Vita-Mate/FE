package com.my.vitamateapp.mySupplement

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.my.vitamateapp.R
import com.my.vitamateapp.repository.SupplementsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReviewListFragment : Fragment() {

    private lateinit var supplementsRepository: SupplementsRepository
    private lateinit var rvAdapter: ReviewListAdapter
    private val reviews = mutableListOf<ReviewItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_review_list, container, false)

        // supplementsRepository 초기화
        supplementsRepository = SupplementsRepository(requireContext())

        val showMoreButton: Button = view.findViewById(R.id.show_review_button)
        val writeReviewButton: Button = view.findViewById(R.id.write_review_button)

        // RecyclerView와 어댑터 설정
        val rv: RecyclerView = view.findViewById(R.id.review_item_rv)
        rvAdapter = ReviewListAdapter(reviews)
        rv.adapter = rvAdapter

        // RecyclerView의 레이아웃 설정
        val layoutManager = LinearLayoutManager(context)
        layoutManager.stackFromEnd = false // 스크롤 막기
        rv.layoutManager = layoutManager

        // API 호출하여 리뷰 목록 불러오기
        loadReviews()

        // "리뷰 전체보기" 버튼 클릭 시 전체 리뷰 액티비티로 이동
        showMoreButton.setOnClickListener {
            val intent = Intent(requireContext(), AllReviewsActivity::class.java)
            startActivity(intent)
        }

        // 리뷰 작성 버튼 클릭 시 리뷰 작성 액티비티로 이동
        writeReviewButton.setOnClickListener {
            val intent = Intent(requireContext(), WriteReviewActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun loadReviews() {
        // 저장된 영양제 ID 가져오기
        val supplementId = getSavedSupplementId()
        if (supplementId == -1) {
            Log.d("ReviewListFragment", "유효하지 않은 영양제 ID")
            return
        }

        // SharedPreferences에서 액세스 토큰 가져오기
        val sharedPreferences = requireContext().getSharedPreferences("saved_user_info", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("accessToken", null)

        if (accessToken.isNullOrEmpty()) {
            Log.e("ReviewListFragment", "액세스 토큰이 유효하지 않습니다.")
            return
        }

        // 코루틴을 사용하여 API 호출
        CoroutineScope(Dispatchers.IO).launch {
            val result = supplementsRepository.getSupplementReviews(accessToken, supplementId)
            withContext(Dispatchers.Main) {
                if (result != null) {
                    // 불러온 리뷰 데이터를 리스트에 추가 (최대 2개만 표시)
                    reviews.clear()
                    reviews.addAll(result.take(2))

                    // 리뷰 리스트를 어댑터에 업데이트
                    rvAdapter.notifyDataSetChanged()

                    // 로그로 모든 리뷰 데이터 출력
                    result.forEach { review ->
                        Log.d("ReviewListFragment", "리뷰: 닉네임=${review.nickname}, 별점=${review.grade}, 내용=${review.content}")
                    }

                } else {
                    Toast.makeText(context, "리뷰 조회 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // 저장된 영양제 ID를 가져오는 함수
    private fun getSavedSupplementId(): Int {
        val sharedPref = requireContext().getSharedPreferences("saved_supplement_info", Context.MODE_PRIVATE)
        val supplementId = sharedPref.getInt("supplementId", -1) // 기본값 -1 (저장된 ID가 없을 때)

        // 로그 추가
        if (supplementId != -1) {
            Log.d("ReviewListFragment", "저장된 영양제 ID: $supplementId")
        } else {
            Log.d("ReviewListFragment", "저장된 영양제 ID가 없습니다.")
        }
        return supplementId
    }
}