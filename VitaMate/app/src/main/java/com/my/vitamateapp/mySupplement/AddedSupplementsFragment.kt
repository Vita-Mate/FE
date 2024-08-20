package com.my.vitamateapp.mySupplement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.my.vitamateapp.R

class AddedSupplementsFragment : Fragment() {

    private lateinit var rvAdapter: AddedSupplementsAdapter
    private val addedSupplements = mutableListOf(
        AddedSupplementModel("Vitamin D", "2024-08-17") // 예시 데이터
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_added_supplements, container, false)

        val rv: RecyclerView = view.findViewById(R.id.addedSupplements_rv)
        rvAdapter = AddedSupplementsAdapter(addedSupplements)
        rv.adapter = rvAdapter
        rv.layoutManager = LinearLayoutManager(context)

        return view
    }
}




