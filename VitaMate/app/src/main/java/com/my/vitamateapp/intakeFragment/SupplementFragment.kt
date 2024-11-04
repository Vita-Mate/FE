package com.my.vitamateapp.intakeFragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.my.vitamateapp.mySupplement.MySupplementActivity
import com.my.vitamateapp.R
import com.my.vitamateapp.mySupplement.AddedSupplementModel
import com.my.vitamateapp.repository.SupplementsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SupplementFragment : Fragment() {
    private lateinit var rvAdapter: TakingSupplementsAdapter
    private val addedSupplements = mutableListOf<AddedSupplementModel>()
    private lateinit var supplementsRepository: SupplementsRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_supplement, container, false)

        // supplementsRepository 초기화 루테인
        supplementsRepository = SupplementsRepository(requireContext())

        // 식단 클릭시 식단 프래그먼트로 이동
        view.findViewById<TextView>(R.id.food).setOnClickListener{
            it.findNavController().navigate(R.id.action_supplementFragment_to_intakeFragment)
        }

        // 물 클릭시 물 프래그먼트로 이동
        view.findViewById<TextView>(R.id.water).setOnClickListener{
            it.findNavController().navigate(R.id.action_supplementFragment_to_waterFragment)
        }

        //+ 누르면 영양제 검색 & 추가할 수 있는 페이지로 이동
        view.findViewById<TextView>(R.id.add_supplement).setOnClickListener {
            val intent = Intent(activity, MySupplementActivity::class.java)
            startActivity(intent)
        }

        //리싸이클러뷰 어댑터 연결
        rvAdapter = TakingSupplementsAdapter(addedSupplements)
        val rv: RecyclerView = view.findViewById(R.id.taking_supplements_list_rv)
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
}