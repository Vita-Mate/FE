package com.my.vitamateapp.mySupplement

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
            val supplement = addedSupplements[position]
            showDeleteDialog(supplement.name, supplement.id, position) // 삭제 다이얼로그 호출
        }
        rv.adapter = rvAdapter
        rv.layoutManager = LinearLayoutManager(context)

        loadTakingSupplements() // 섭취중인 영양제 반환 API 호출

        return view
    }

    // 삭제 확인 다이얼로그 표시
    private fun showDeleteDialog(supplementName: String, supplementId: Int, position: Int) {
        val dialogView = layoutInflater.inflate(R.layout.supplement_delete, null)

        // 다이얼로그에 영양제 이름 설정
        dialogView.findViewById<TextView>(R.id.supplementName).text = supplementName

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .create()

        // 둥근 모서리 배경 설정 오메가
        dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_background)

        // 네 버튼 클릭 시 삭제 처리
        dialogView.findViewById<Button>(R.id.button_yes).setOnClickListener {
            deleteSupplement(supplementId, position)
            dialog.dismiss()
        }

        // 아니오 버튼 클릭 시 다이얼로그 닫기
        dialogView.findViewById<Button>(R.id.button_no).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    // 섭취 중인 영양제 리스트 가져오는 API 연동
    private fun loadTakingSupplements(page: Int = 0) {
        val sharedPreferences = requireActivity().getSharedPreferences("saved_user_info", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("accessToken", null)

        if (accessToken.isNullOrEmpty()) {
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


