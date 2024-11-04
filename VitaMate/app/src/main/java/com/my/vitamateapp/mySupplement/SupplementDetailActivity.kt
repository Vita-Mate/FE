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
import com.my.vitamateapp.databinding.ActivitySupplementDetailBinding
import com.my.vitamateapp.repository.SupplementsRepository
import kotlinx.coroutines.launch

class SupplementDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySupplementDetailBinding
    private lateinit var supplementsRepository: SupplementsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySupplementDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supplementsRepository = SupplementsRepository(this)

        val supplementId = getSavedSupplementId()
        getSupplementDetail(supplementId)

        binding.addButton.setOnClickListener {
            val intent = Intent(this, SupplementStartActivity::class.java)
            startActivity(intent)
        }
    }

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

                Glide.with(this@SupplementDetailActivity)
                    .load(result.nutrientInfoImageUrl)
                    .into(binding.supplementDetail)

                // recommendList가 null이 아닐 때만 take()를 호출
                val relatedProducts = detail.result.recommendList?.take(5) ?: emptyList()
                val adapter = RelatedProductAdapter(relatedProducts)
                binding.relatedProductsRv.adapter = adapter
                binding.relatedProductsRv.layoutManager = LinearLayoutManager(this@SupplementDetailActivity)

                Log.d("SupplementDetailActivity", "영양제 상세 정보 조회 성공: $detail")
            } ?: run {
                Log.e("SupplementDetailActivity", "영양제 상세 정보 조회 실패: null 반환")
                Toast.makeText(this@SupplementDetailActivity, "영양제 상세 정보 조회 실패", Toast.LENGTH_SHORT).show()
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
}



