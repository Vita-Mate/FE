package com.my.vitamateapp.intakeFragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.my.vitamateapp.mySupplement.MySupplementActivity
import com.my.vitamateapp.R


class SupplementFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_supplement, container, false)

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

        return view
    }


}