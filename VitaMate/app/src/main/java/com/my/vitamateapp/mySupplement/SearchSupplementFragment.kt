package com.my.vitamateapp.mySupplement

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.my.vitamateapp.R
import com.my.vitamateapp.mySupplement.SearchedSupplementModel


class SearchSupplementFragment : Fragment() {

    private lateinit var rvAdapter: SearchedSupplementsAdapter
    private var searchedSupplements = ArrayList<SearchedSupplementModel>() // 이름 변경

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_searched_supplement, container, false)

        val rv: RecyclerView = view.findViewById(R.id.serachedSupplements_rv)

        // SearchSupplementFragment에서 데이터를 받는 부분
        arguments?.let {
            searchedSupplements = it.getParcelableArrayList<SearchedSupplementModel>("supplements") ?: ArrayList()
        }

        // 어댑터에 saveSupplementId 함수를 전달
        rvAdapter = SearchedSupplementsAdapter(searchedSupplements, requireContext()) { supplementId ->
            saveSupplementId(supplementId) // 아이템 클릭 시 영양제 ID 저장
            Log.d("MySupplementActivity", "SupplementId : $supplementId")
        }

        rv.adapter = rvAdapter
        rv.layoutManager = LinearLayoutManager(context)

        return view
    }

    // 클릭한 영양제 ID 저장 함수
    private fun saveSupplementId(supplementId: Int) {
        val sharedPref = requireActivity().getSharedPreferences("saved_supplement_info", Context.MODE_PRIVATE)

        // 새로운 영양제ID를 바로 저장
        with(sharedPref.edit()) {
            putInt("supplementId", supplementId)
            apply()
        }
        Log.d("MySupplementActivity", "New SupplementId saved: $supplementId")
    }
}




