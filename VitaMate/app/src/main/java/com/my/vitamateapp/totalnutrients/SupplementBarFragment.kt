package com.my.vitamateapp.totalnutrients

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.my.vitamateapp.R
import com.my.vitamateapp.mySupplement.NutrientInfo
import com.my.vitamateapp.repository.SupplementsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SupplementBarFragment : Fragment() {

    private lateinit var supplementsRepository: SupplementsRepository
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SupplementBarAdapter
    private lateinit var extendedBarButton: ImageView
    private var isExpanded = false // 아이템 확장 여부 상태 변수
    private var supplementList = listOf<NutrientInfo>() // 전체 데이터를 담을 리스트

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_supplement_bar, container, false)

        // supplementsRepository 초기화
        supplementsRepository = SupplementsRepository(requireContext())

        recyclerView = view.findViewById(R.id.supplement_bar_rv)
        recyclerView.layoutManager = LinearLayoutManager(context)

        extendedBarButton = view.findViewById(R.id.extended_bar)

        extendedBarButton.setOnClickListener {
            toggleSupplementList()  // 클릭 시 리스트 토글
        }

        fetchNutrientData()

        return view
    }

    private fun fetchNutrientData() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                Log.d("SupplementBarFragment", "Fetching nutrient data...")

                val sharedPreferences = requireContext().getSharedPreferences("saved_user_info", Context.MODE_PRIVATE)
                val accessToken = sharedPreferences.getString("accessToken", null) ?: ""

                if (accessToken.isEmpty()) {
                    Log.e("SupplementBarFragment", "Access token is missing!")
                    return@launch
                }

                // API 호출 및 응답 처리
                supplementList = supplementsRepository.getTakingNutrients(accessToken)

                Log.d("SupplementBarFragment", "Fetched ${supplementList.size} supplements from API.")

                // 기본 3개의 아이템만 표시
                val initialSupplementList = supplementList.take(3)

                withContext(Dispatchers.Main) {
                    // RecyclerView 어댑터 설정
                    adapter = SupplementBarAdapter(initialSupplementList)
                    recyclerView.adapter = adapter
                    Log.d("SupplementBarFragment", "Adapter set with fetched data.")

                    // RecyclerView의 기본 높이를 360dp로 유지
                    adjustRecyclerViewHeight(supplementList.size.coerceAtLeast(3)) // 최소 3개 크기 유지
                }
            } catch (e: Exception) {
                Log.e("SupplementBarFragment", "Error fetching nutrient data: ${e.message}")
            }
        }
    }


    private fun toggleSupplementList() {
        // 아이템 확장/축소 상태 변경
        isExpanded = !isExpanded

        // 리스트 변경 (전체 아이템으로 확장/축소)
        val newList = if (isExpanded) supplementList else supplementList.take(3)

        // 어댑터 업데이트
        adapter.updateList(newList)

        // RecyclerView 높이 변경
        adjustRecyclerViewHeight(newList.size)

        // 버튼 아이콘 변경
        val newIcon = if (isExpanded) R.drawable.arrow_up else R.drawable.arrow_down
        extendedBarButton.setImageResource(newIcon)
    }

    private fun adjustRecyclerViewHeight(itemCount: Int) {
        val itemHeight = resources.getDimensionPixelSize(R.dimen.supplement_item_height) // 한 아이템 높이
        val baseHeight = resources.getDimensionPixelSize(R.dimen.supplement_bar_fixed_height) // 기본 높이 (360dp)

        val newHeight = if (isExpanded) {
            // 확장 상태에서는 전체 아이템 높이 반영
            itemHeight * itemCount
        } else {
            // 축소 상태에서는 360dp 유지
            baseHeight
        }

        // RecyclerView 높이 설정
        val layoutParams = recyclerView.layoutParams
        layoutParams.height = newHeight
        recyclerView.layoutParams = layoutParams
    }
}
