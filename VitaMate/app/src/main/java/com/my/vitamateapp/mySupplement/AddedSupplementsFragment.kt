package com.my.vitamateapp.mySupplement

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.my.vitamateapp.R
import com.my.vitamateapp.repository.SupplementsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddedSupplementsFragment : Fragment() {

    private lateinit var rvAdapter: AddedSupplementsAdapter
    private val addedSupplements = mutableListOf<AddedSupplementModel>() // 추가된 영양제 모델 리스트
    private lateinit var supplementsRepository: SupplementsRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_added_supplements, container, false)

        // supplementsRepository 초기화
        supplementsRepository = SupplementsRepository(requireContext())

        val rv: RecyclerView = view.findViewById(R.id.addedSupplements_rv)
        rvAdapter = AddedSupplementsAdapter(addedSupplements) { position ->
            val supplementId = addedSupplements[position].id
            deleteSupplement(supplementId, position) // 삭제 함수 호출
        }
        rv.adapter = rvAdapter
        rv.layoutManager = LinearLayoutManager(context)

        loadTakingSupplements() // 섭취중인 영양제반환 api

        return view
    }

    // 섭취 중인 영양제 리스트 가져오는 API 연동
    private fun loadTakingSupplements(page: Int = 0) {
        val sharedPreferences = requireActivity().getSharedPreferences("saved_user_info", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("accessToken", null)

        if (accessToken.isNullOrEmpty()) {
            Toast.makeText(context, "액세스 토큰이 유효하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            val supplements = supplementsRepository.getTakingSupplements(accessToken, page)
            addedSupplements.clear()
            addedSupplements.addAll(supplements)

            withContext(Dispatchers.Main) {
                rvAdapter.notifyDataSetChanged()
            }
        }
    }

    // 섭취 중인 영양제 삭제 API 연동
    private fun deleteSupplement(supplementId: Int, position: Int) {
        val sharedPreferences = requireActivity().getSharedPreferences("saved_user_info", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("accessToken", null)

        if (accessToken.isNullOrEmpty()) {
            Toast.makeText(context, "액세스 토큰이 유효하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            val isDeleted = supplementsRepository.deleteSupplement(accessToken, supplementId)
            if (isDeleted) {
                addedSupplements.removeAt(position)
                withContext(Dispatchers.Main) {
                    rvAdapter.notifyDataSetChanged()
                }
            }
        }
    }
}



