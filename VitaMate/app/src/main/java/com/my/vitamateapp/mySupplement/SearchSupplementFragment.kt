package com.my.vitamateapp.mySupplement

import android.os.Bundle
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



        rvAdapter = SearchedSupplementsAdapter(searchedSupplements, requireContext())
        rv.adapter = rvAdapter
        rv.layoutManager = LinearLayoutManager(context)

        return view
    }
}





