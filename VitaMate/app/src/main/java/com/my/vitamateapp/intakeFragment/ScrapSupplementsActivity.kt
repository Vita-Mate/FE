package com.my.vitamateapp.intakeFragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.ActivityScrapSupplementsBinding
import com.my.vitamateapp.mySupplement.AddedSupplementModel
import com.my.vitamateapp.mySupplement.MySupplementActivity
import com.my.vitamateapp.mySupplement.NutrientInfo
import com.my.vitamateapp.mySupplement.ScrapResult
import com.my.vitamateapp.repository.SupplementsRepository
import com.my.vitamateapp.totalnutrients.SupplementBarAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScrapSupplementsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScrapSupplementsBinding
    private lateinit var supplementsRepository: SupplementsRepository
    private lateinit var scrapRvAdapter: ScrapSupplementsAdapter
    private lateinit var addedRvAdapter: TakingSupplementsAdapter
    private lateinit var simulationAdapter: SupplementBarAdapter
    private lateinit var extendedBarButton: ImageView
    private var isExpanded = false // 아이템 확장 여부 상태 변수
    private var supplementList = listOf<NutrientInfo>() // 전체 데이터를 담을 리스트

    // 세개의 리스트를 별도로 관리
    private val scrapedSupplements = mutableListOf<ScrapResult>()  // 섭취 중인 영양제
    private val addedSupplements = mutableListOf<AddedSupplementModel>() // 스크랩한 영양제
    private val simulationSupplements = mutableListOf<NutrientInfo>() // 시뮬레이션 영양제 막대바

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScrapSupplementsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 이전 페이지로 이동
        binding.preButton.setOnClickListener {
            goPre()
        }

        // SupplementsRepository 초기화
        supplementsRepository = SupplementsRepository(this)

        // 스크랩한 영양제 리사이클러뷰 설정
        scrapRvAdapter = ScrapSupplementsAdapter(scrapedSupplements) { supplementId ->
            // 삭제 버튼 클릭 시 실행될 로직
            removeSupplementFromScrap(supplementId)
        }
        binding.scrapSupplementRv.adapter = scrapRvAdapter
        binding.scrapSupplementRv.layoutManager = LinearLayoutManager(this)

        // 추가한 영양제 리사이클러뷰 설정
        addedRvAdapter = TakingSupplementsAdapter(addedSupplements)
        binding.takingSupplementRv.adapter = addedRvAdapter
        binding.takingSupplementRv.layoutManager = LinearLayoutManager(this)

        // 시뮬레이션 리사이클러뷰 설정
        simulationAdapter = SupplementBarAdapter(simulationSupplements.take(3)) // 초기 3개만 보여주기
        binding.supplementBarRv.adapter = simulationAdapter
        binding.supplementBarRv.layoutManager = LinearLayoutManager(this)

        // 시뮬레이션 프래그먼트 확장
        extendedBarButton = binding.extendedBar

        extendedBarButton.setOnClickListener {
            toggleSupplementList()  // 클릭 시 리스트 토글
        }

        // 스크랩영양제 추가를 위해 영양제검색페이지로 전환
        binding.addSupplement.setOnClickListener{
            val intent = Intent(this, MySupplementActivity::class.java)
            intent.putExtra("entry_mode", "scrap") // "scrap" 모드로 전달
            startActivity(intent)
        }

        // 스크랩한 영양제 목록 조회
        getScrapSupplements()

        // 추가한 영양제 목록 조회
        loadTakingSupplements()

        // 시뮬레이션 결과 조회
        getSimulationSupplements()
    }

    // 스크랩한 영양제 조회 API 연동
    private fun getScrapSupplements() {
        val sharedPreferences = getSharedPreferences("saved_user_info", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("accessToken", null)

        if (accessToken.isNullOrEmpty()) {
            Log.e("ScrapSupplementsActivity", "액세스 토큰이 null 또는 비어 있음")
            return
        }

        lifecycleScope.launch {
            val supplementsList: List<ScrapResult> = supplementsRepository.getScrapSupplements(accessToken)
            withContext(Dispatchers.Main) {
                if (supplementsList.isNotEmpty()) {
                    Log.d("ScrapSupplementsActivity", "스크랩한 영양제 목록 가져오기 성공")
                    scrapedSupplements.clear()
                    scrapedSupplements.addAll(supplementsList)
                    scrapRvAdapter.notifyDataSetChanged() // 스크랩한 영양제 어댑터에 데이터 변경 알림
                } else {
                    Log.d("ScrapSupplementsActivity", "스크랩한 영양제가 없음")
                }
            }
        }
    }

    // 섭취 중인 영양제 리스트 가져오는 API 연동
    private fun loadTakingSupplements(page: Int = 0) {
        val sharedPreferences = getSharedPreferences("saved_user_info", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("accessToken", null)

        if (accessToken.isNullOrEmpty()) {
            Log.e("ScrapSupplementsActivity", "액세스 토큰이 null 또는 비어 있음")
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            val supplements = supplementsRepository.getTakingSupplements(accessToken, page)
            addedSupplements.clear()
            addedSupplements.addAll(supplements)

            withContext(Dispatchers.Main) {
                addedRvAdapter.notifyDataSetChanged() // 추가한 영양제 어댑터에 데이터 변경 알림
            }
        }
    }

    // 추가영양제 + 스크랩영양제 시뮬레이션 API 연동
    private fun getSimulationSupplements() {
        val accessToken = getSharedPreferences("saved_user_info", Context.MODE_PRIVATE)
            .getString("accessToken", null)

        if (accessToken.isNullOrEmpty()) {
            Log.e("ScrapSupplementsActivity", "액세스 토큰이 null 또는 비어 있음")
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            Log.d("ScrapSupplementsActivity", "API 호출 시작") // API 호출 시작 확인
            val supplements = supplementsRepository.getSimulatedSupplementsNutrients(accessToken)

            withContext(Dispatchers.Main) {
                if (supplements != null) {
                    Log.d("ScrapSupplementsActivity", "API 호출 성공 : ${supplements.size}개 아이템 수신") // 데이터 받아오기 성공
                    supplements.forEach { supplement ->
                        Log.d("ScrapSupplementsActivity", "받은 아이템: $supplement") // 각 아이템의 상세 데이터 출력
                    }
                    supplementList = supplements // 전체 데이터를 저장해둠
                    simulationSupplements.clear()
                    simulationSupplements.addAll(supplements.take(3)) // 초기 3개 아이템만 추가

                    simulationAdapter.updateList(simulationSupplements) // 초기 3개의 아이템으로 갱신
                    adjustRecyclerViewHeight(simulationSupplements.size) // 초기 크기로 조정

                } else {
                    Log.e("ScrapSupplementsActivity", "API 호출 실패 - supplements가 null") // 데이터가 null일 때
                }
            }
        }
    }

    // 영양제 스크랩 삭제 API 호출
    private fun removeSupplementFromScrap(supplementId: Int) {
        val sharedPreferences = getSharedPreferences("saved_user_info", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("accessToken", null)

        if (accessToken.isNullOrEmpty()) {
            Log.e("ScrapSupplementsActivity", "액세스 토큰이 유효하지 않습니다.")
            return
        }

        lifecycleScope.launch {
            Log.d("ScrapSupplementsActivity", "영양제 스크랩 삭제 요청: supplementId = $supplementId")

            // 스크랩 삭제 API 호출 루테인
            val success = supplementsRepository.deleteSupplementScrap(accessToken, supplementId)

            if (success) {
                // 삭제된 아이템을 리스트에서 제거
                scrapedSupplements.removeAll { it.supplementId == supplementId }
                scrapRvAdapter.notifyDataSetChanged() // 어댑터 갱신
            } else {
                Log.e("ScrapSupplementsActivity", "스크랩 삭제 실패")
            }
        }
    }

    // 아이콘 바꿈 함수
    private fun toggleSupplementList() {
        isExpanded = !isExpanded
        val newList = if (isExpanded) supplementList else supplementList.take(3)
        simulationSupplements.clear()
        simulationSupplements.addAll(newList) // 리스트 갱신
        simulationAdapter.updateList(newList) // 어댑터 갱신
        adjustRecyclerViewHeight(newList.size) // 크기 조정

        val newIcon = if (isExpanded) R.drawable.arrow_up else R.drawable.arrow_down
        extendedBarButton.setImageResource(newIcon)
    }

    // 아이템 수에 맞게 프래그먼트 크기 조절 함수
    private fun adjustRecyclerViewHeight(itemCount: Int) {
        val itemHeight = resources.getDimensionPixelSize(R.dimen.supplement_item_height) // 한 아이템 높이
        val maxHeight = resources.getDimensionPixelSize(R.dimen.supplement_bar_fixed_height) // 기본 360dp 높이

        val newHeight = if (isExpanded) {
            // 확장 상태: 전체 아이템의 높이 반영
            itemHeight * itemCount
        } else {
            // 축소 상태: 최대 높이(360dp)로 설정
            maxHeight
        }

        // RecyclerView의 높이를 동적으로 설정
        val layoutParams = binding.supplementBarRv.layoutParams
        layoutParams.height = newHeight
        binding.supplementBarRv.layoutParams = layoutParams
    }

    // 이전페이지 함수
    private fun goPre() {
        Log.d("ScrapSupplementsActivity", "goPre: 이전 페이지로 이동")
        finish()
    }
}



