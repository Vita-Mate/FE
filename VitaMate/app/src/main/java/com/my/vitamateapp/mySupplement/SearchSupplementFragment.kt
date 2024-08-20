package com.my.vitamateapp.mySupplement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.my.vitamateapp.R

class SearchSupplementFragment : Fragment() {

    private lateinit var rvAdapter: SearchedSupplementsAdapter
    private val supplements = mutableListOf(
        SearchedSupplementModel("Vitamin C", "Aids in immune function."),
        SearchedSupplementModel("Omega-3", "Supports heart health."),
        SearchedSupplementModel("Zinc", "Important for enzyme function.")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_searched_supplement, container, false)

        val rv: RecyclerView = view.findViewById(R.id.serachedSupplements_rv)

        rvAdapter = SearchedSupplementsAdapter(supplements, requireContext())
        rv.adapter = rvAdapter
        rv.layoutManager = LinearLayoutManager(context)

        return view
    }
}






