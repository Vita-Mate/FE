package com.my.vitamateapp.intakeFragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.my.vitamateapp.R
import com.my.vitamateapp.foodRecord.BreakfastRecordActivity
import com.my.vitamateapp.foodRecord.DinnerRecordActivity
import com.my.vitamateapp.foodRecord.LunchRecordActivity
import com.my.vitamateapp.foodRecord.SnackRecordActivity


class IntakeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_intake, container, false)


        //영양제 클릭시 영양제 프래그먼트로 이동
        view.findViewById<TextView>(R.id.supplement).setOnClickListener{
            it.findNavController().navigate(R.id.action_intakeFragment_to_supplementFragment)
        }

        //물 클릭시 물 프래그먼트로 이동
        view.findViewById<TextView>(R.id.water).setOnClickListener{
            it.findNavController().navigate(R.id.action_intakeFragment_to_waterFragment)
        }


        // 아침 버튼 클릭 시 아침 기록 액티비티로 이동
        view.findViewById<View>(R.id.breakfast_record).setOnClickListener {
            startActivity(Intent(activity, BreakfastRecordActivity::class.java))
        }

        // 점심 버튼 클릭 시 점심 기록 액티비티로 이동
        view.findViewById<View>(R.id.lunch_record).setOnClickListener {
            startActivity(Intent(activity, LunchRecordActivity::class.java))
        }

        // 저녁 버튼 클릭 시 저녁 기록 액티비티로 이동
        view.findViewById<View>(R.id.dinner_record).setOnClickListener {
            startActivity(Intent(activity, DinnerRecordActivity::class.java))
        }

        // 간식 버튼 클릭 시 간식 기록 액티비티로 이동
        view.findViewById<View>(R.id.snack_record).setOnClickListener {
            startActivity(Intent(activity, SnackRecordActivity::class.java))
        }

        return view
    }


}