package com.my.vitamateapp.Challenge

import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.my.vitamateapp.Api.GetExerciseMyRecordApi
import com.my.vitamateapp.Api.RetrofitInstance
import com.my.vitamateapp.ChallengeDTO.GetExerciseMyRecordResponse
import com.my.vitamateapp.ChallengeDTO.GetMyResult
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.FragmentMyExerciseRecordBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class FragmentMyExerciseRecord : Fragment() {

    private lateinit var binding: FragmentMyExerciseRecordBinding
    private lateinit var recyclerViewAdapter: MyRecordAdapter
    private var selectedDate: String? = null  // 문자열로 날짜 저장
    private var challengeId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        challengeId = arguments?.getLong("challengeId", -1L)
        Log.d("FragmentMyExerciseRecord", "Received challengeId in onCreate: $challengeId")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyExerciseRecordBinding.inflate(inflater, container, false)

        // arguments에서 challengeId를 가져옵니다
        challengeId = arguments?.getLong("challengeId", -1L)  // 기본값 -1L
        Log.d("FragmentMyExerciseRecord", "Received challengeId in onCreateView: $challengeId")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 전달받은 challengeId 확인
        Log.d("FragmentMyExerciseRecord", "Received challengeId in onViewCreated: $challengeId")

        // 버튼 클릭 리스너 설정
        binding.addMyRecord.setOnClickListener {
            Log.d("FragmentMyExerciseRecord", "addMyRecord button clicked")
            showChallengeBottomSheetDialog(challengeId ?: -1L)  // challengeId를 BottomSheetDialogFragment로 전달
        }


        // RecyclerView 설정
        val recyclerView = binding.myRecordRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // RecyclerView Adapter 초기화
        recyclerViewAdapter = MyRecordAdapter()
        recyclerView.adapter = recyclerViewAdapter

        // challengeId가 유효한지 확인하고 API 호출
        challengeId?.let {
            fetchMyRecord(it) // Long을 String으로 변환하여 전달
        } ?: run {
            showError("Invalid challengeId")
        }

    }

    // 선택한 날짜를 사용하여 운동 기록을 불러옵니다.
    private fun fetchMyRecord(challengeId: Long) {
        val api = RetrofitInstance.getInstance().create(GetExerciseMyRecordApi::class.java)
        val accessToken = getAccessToken(requireContext()) ?: run {
            Log.w(TAG, "Access Token이 null입니다.")
            return
        }

        // 날짜를 포함한 API 호출
        val selectedDateString = selectedDate ?: getCurrentDate()

        Log.d(TAG, "Sending request with Token: Bearer $accessToken, challengeId: $challengeId, date: $selectedDateString")

        api.getExerciseMyRecord("Bearer $accessToken", challengeId, selectedDateString).enqueue(object : Callback<GetExerciseMyRecordResponse> {
            override fun onResponse(call: Call<GetExerciseMyRecordResponse>, response: Response<GetExerciseMyRecordResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.isSuccess == false) {
                        updateUI(responseBody.result)
                        Log.d("Success: ", "MyExerciseRecord : $responseBody")
                    } else {
                        showError(responseBody?.message ?: "Unknown error")
                    }
                } else {
                    val errorBody = response.errorBody()?.string() // 서버의 오류 메시지 확인
                    Log.e(TAG, "Error: $errorBody")
                    showError("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<GetExerciseMyRecordResponse>, t: Throwable) {
                showError("Failed to load my record: ${t.message}")
            }
        })
    }

    private fun showChallengeBottomSheetDialog(challengeId: Long) {
        val bottomSheetFragment = ChallengeBottomSheetDialogFragment().apply {
            arguments = Bundle().apply {
                putLong("challengeId", challengeId)
            }
        }
        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
    }

    // 현재 날짜를 반환합니다.
    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    // UI 업데이트
    private fun updateUI(result: List<GetMyResult>) {
        // RecyclerView Adapter에 데이터 전달
        recyclerViewAdapter.submitList(result)
    }

    // 에러 메시지를 표시합니다.
    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        Log.e("FragmentMyExerciseRecord", message)
    }

    // SharedPreferences에서 AccessToken을 가져옵니다.
    private fun getAccessToken(context: Context): String? {
        val sharedPref = context.getSharedPreferences("saved_user_info", Context.MODE_PRIVATE)
        return sharedPref.getString("accessToken", null)
    }
}

