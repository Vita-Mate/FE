package com.my.vitamateapp.intakeFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.my.vitamateapp.R


class WaterFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_water, container, false)

        // 식단 클릭시 식단 프래그먼트로 이동
        view.findViewById<TextView>(R.id.food).setOnClickListener{
            it.findNavController().navigate(R.id.action_waterFragment_to_intakeFragment)
        }

        // 영양제 클릭시 영양제 프래그먼트로 이동
        view.findViewById<TextView>(R.id.supplement).setOnClickListener{
            it.findNavController().navigate(R.id.action_waterFragment_to_supplementFragment)
        }

        return view
    }


}