package com.my.vitamateapp.mySupplement

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.ActivitySupplementDetailBinding
import com.my.vitamateapp.repository.SupplementsRepository
import kotlinx.coroutines.launch

class SupplementDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySupplementDetailBinding
    private lateinit var supplementsRepository: SupplementsRepository
    private var isScraped = false  // 스크랩 상태를 추적

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySupplementDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supplementsRepository = SupplementsRepository(this)

        val supplementId = getSavedSupplementId()
        getSupplementDetail(supplementId)

        binding.addButton.setOnClickListener {
            Log.d("AddButton", "Add button clicked")
            val intent = Intent(this, SupplementStartActivity::class.java)
            startActivity(intent)
        }


        // 연관상품 이미지 추가
        val imageResources = listOf(
            R.drawable.supplement1,
            R.drawable.supplement2,
            R.drawable.supplement3,
            R.drawable.supplement4,
            R.drawable.supplement5
        )

        // RecyclerView에 어댑터 연결
        val adapter = RelatedProductAdapter(imageResources)
        binding.relatedProductsRv.adapter = adapter
        binding.relatedProductsRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // < 버튼 클릭시 이전 페이지로 이동
        binding.preButton.setOnClickListener {
            goPre()
        }

        // 스크랩 버튼 클릭 리스너
        binding.scrapButton.setOnClickListener {
            if (isScraped) {
                // 스크랩 삭제
                removeSupplementFromScrap(supplementId)
            } else {
                // 스크랩 추가
                addSupplementToScrap(supplementId)
            }
        }
    }

    // 영양제 스크랩 추가 API 호출
    private fun addSupplementToScrap(supplementId: Int) {
        val sharedPreferences = getSharedPreferences("saved_user_info", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("accessToken", null)

        if (accessToken.isNullOrEmpty()) {
            Log.e("SupplementDetailActivity", "액세스 토큰이 유효하지 않습니다.")
            return
        }

        lifecycleScope.launch {
            Log.d("SupplementDetailActivity", "영양제 스크랩 추가 요청: supplementId = $supplementId")

            // 스크랩 추가 API 호출
            val success = supplementsRepository.addSupplementScrap(accessToken, supplementId)

            if (success) {  // 반환된 Boolean 값이 true일 경우
                isScraped = true
                binding.scrapButton.setImageResource(R.drawable.bookmark_color) // 스크랩 버튼 이미지 변경
                Log.d("SupplementDetailActivity", "스크랩 추가 성공")
            } else {
                Log.e("SupplementDetailActivity", "스크랩 추가 실패")
            }
        }
    }

    // 영양제 스크랩 삭제 API 호출
    private fun removeSupplementFromScrap(supplementId: Int) {
        val sharedPreferences = getSharedPreferences("saved_user_info", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("accessToken", null)

        if (accessToken.isNullOrEmpty()) {
            Log.e("SupplementDetailActivity", "액세스 토큰이 유효하지 않습니다.")
            return
        }

        lifecycleScope.launch {
            Log.d("SupplementDetailActivity", "영양제 스크랩 삭제 요청: supplementId = $supplementId")

            // 스크랩 삭제 API 호출
            val success = supplementsRepository.deleteSupplementScrap(accessToken, supplementId)

            if (success) {
                isScraped = false
                binding.scrapButton.setImageResource(R.drawable.bookmark_white)  // 스크랩 버튼 이미지 변경
            } else {
                Log.e("SupplementDetailActivity", "스크랩 삭제 실패")
            }
        }
    }

    // 영양제 상세정보 조회 API
    private fun getSupplementDetail(supplementId: Int) {
        val sharedPreferences = getSharedPreferences("saved_user_info", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("accessToken", null)

        if (accessToken.isNullOrEmpty()) {
            Log.e("SupplementDetailActivity", "액세스 토큰이 유효하지 않습니다.")
            return
        }

        lifecycleScope.launch {
            Log.d("SupplementDetailActivity", "영양제 상세 정보 조회 요청: supplementId = $supplementId")

            val supplementDetail: GetDetailResponse? = supplementsRepository.getSupplementsDetail(accessToken, supplementId)

            supplementDetail?.let { detail ->
                val result = detail.result
                binding.supplementNameDetail.text = result.name

                // 영양제 이름을 SharedPreferences에 바로 저장
                getSharedPreferences("saved_supplement_info", Context.MODE_PRIVATE).edit()
                    .putString("supplementName", result.name)
                    .apply()

                Glide.with(this@SupplementDetailActivity)
                    .load(result.nutrientInfoImageUrl)
                    .into(binding.supplementDetail)
                Log.d("SupplementDetailActivity", "영양제 상세 정보 조회 성공: $detail")
            } ?: run {
                Log.e("SupplementDetailActivity", "영양제 상세 정보 조회 실패: null 반환")

            }
        }
    }

    private fun getSavedSupplementId(): Int {
        val supplementId = getSharedPreferences("saved_supplement_info", Context.MODE_PRIVATE)
            .getInt("supplementId", -1)

        Log.d("SupplementDetailActivity", if (supplementId != -1) {
            "저장된 영양제 ID: $supplementId"
        } else {
            "저장된 영양제 ID가 없습니다."
        })

        return supplementId
    }

    // 이전 페이지로 이동 함수 비타
    private fun goPre() {
        finish()
    }
}




