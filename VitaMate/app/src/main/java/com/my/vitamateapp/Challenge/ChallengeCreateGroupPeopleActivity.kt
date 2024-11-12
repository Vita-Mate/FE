package com.my.vitamateapp.Challenge


import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.my.vitamateapp.Api.CreateChallengeApi
import com.my.vitamateapp.Api.RetrofitInstance
import com.my.vitamateapp.HomeActivity
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.ActivityChallegneCreateGroupPeopleBinding
import com.my.vitamateapp.registerPage.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Tag

class ChallengeCreateGroupPeopleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChallegneCreateGroupPeopleBinding

    // Store Intent data as class-level variables
    private lateinit var category: String
    private lateinit var title: String
    private lateinit var description: String
    private lateinit var startDate: String
    private lateinit var duration: Duration // Duration을 Enum으로 저장
    private var weekFrequency: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_challegne_create_group_people)

        // Get data from Intent
        category = intent.getStringExtra("category") ?: run {
            showToast("Category is missing.")
            finish()
            return
        }
        title = intent.getStringExtra("title") ?: ""
        description = intent.getStringExtra("description") ?: ""
        startDate = intent.getStringExtra("startDate") ?: run {
            showToast("Start date is missing.")
            finish()
            return
        }

        // EditText에서 duration 값을 읽어와서 Duration enum으로 변환
        val durationString = intent.getStringExtra("duration") ?: run {
            showToast("Duration is missing.")
            finish()
            return
        }
        duration = Duration.values().find { it.name.equals(durationString, ignoreCase = true) } ?: run {
            showToast("유효한 기간을 입력하세요.")
            finish()
            return
        }
        weekFrequency = intent.getIntExtra("weekFrequency", 0)

        // Set up click listeners
        binding.preButton1.setOnClickListener { finish() }
        binding.groupPeoplePrebtn.setOnClickListener {
            startActivity(Intent(this, ChallengeCreateGroupObjectiveActivity::class.java))
        }
        binding.groupSubmitButton.setOnClickListener { saveDataToDatabase() }
    }

    private fun saveDataToDatabase() {

        val maxParticipants = binding.maxChallGroupPeople.text.toString().toIntOrNull()
        val minParticipants = binding.minChallGroupPeople.text.toString().toIntOrNull()

        // Validate inputs
        if (maxParticipants == null || minParticipants == null  || title.isEmpty() || description.isEmpty()) {
            showToast("모든 필드를 올바르게 입력하세요.")
            return
        }

        // Validate participant counts
        if (minParticipants < 1 || minParticipants > 10) {
            showToast("최소 참가자는 1명 이상, 10명 이하이어야 합니다.")
            return
        }

        if (maxParticipants < 2 || maxParticipants > 10) {
            showToast("최대 참가자는 2명 이상, 10명 이하이어야 합니다.")
            return
        }

        if (minParticipants > maxParticipants) {
            showToast("최소 참가자는 최대 참가자 수보다 작거나 같아야 합니다.")
            return
        }

        // Create challenge request
        val categoryEnum = Category.values().find { it.name == category } ?: return

        val createChallengeRequest = CreateChallengeRequest(
            category = categoryEnum,
            title = title,
            description = description,
            startDate = startDate,
            duration = duration,
            maxParticipants = maxParticipants,
            minParticipants = minParticipants,
            weeklyFrequency = weekFrequency
        )

        // Log each field in CreateChallengeRequest
        Log.d("CreateChallengeRequest", "Category: ${createChallengeRequest.category}")
        Log.d("CreateChallengeRequest", "Title: ${createChallengeRequest.title}")
        Log.d("CreateChallengeRequest", "Description: ${createChallengeRequest.description}")
        Log.d("CreateChallengeRequest", "Start Date: ${createChallengeRequest.startDate}")
        Log.d("CreateChallengeRequest", "Duration: ${createChallengeRequest.duration}")
        Log.d("CreateChallengeRequest", "Max Participant: ${createChallengeRequest.maxParticipants}")
        Log.d("CreateChallengeRequest", "Min Participant: ${createChallengeRequest.minParticipants}")
        Log.d("CreateChallengeRequest", "Weekly Frequency: ${createChallengeRequest.weeklyFrequency}")

        // API 호출
        val api = RetrofitInstance.getInstance().create(CreateChallengeApi::class.java)

        // Access Token 가져오기
        val accessToken = getAccessToken(this)
        if (accessToken == null) {
            showToast("Access Token이 없습니다. 로그인을 다시 시도하세요.")
            return
        }else Log.i(TAG, "액세스 토큰: ${accessToken}")

        if (!createChallengeRequest.isValid()) {
            showToast("참가자 수가 유효하지 않습니다. 최소: 1, 최대: 10.")
            return
        }

        // API 호출
        api.createChallenge("Bearer $accessToken", createChallengeRequest).enqueue(object : Callback<CreateChallengeResponse> {
            override fun onResponse(call: Call<CreateChallengeResponse>, response: Response<CreateChallengeResponse>) {
                if (response.isSuccessful && response.body()?.isSuccess == false) {
                    Log.d("Response Code", "Response code: ${response.code()}")
                    Log.d("Response Body", "Response body: ${response.body()?.toString()}")

                    // 응답에서 challengeId를 받아옴
                    val challengeId = response.body()?.result?.challengeId // result 객체를 통해 challengeId에 접근

                    // challengeId가 null이 아니면 SharedPreferences에 저장
                    if (challengeId != null) {
                        saveChallengeIdToPreferences(challengeId)
                    }else {
                        showToast("챌린지 ID를 받아올 수 없습니다.")
                    }

                    val intent = Intent(this@ChallengeCreateGroupPeopleActivity, HomeActivity::class.java).apply {
                        putExtra("message", "챌린지가 등록되었습니다.")
                        }
                    startActivity(intent)
                    finish()
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "챌린지 등록에 실패했습니다."
                    Log.d("Error:","errorMessage ${response.code()}: $errorMessage") // 응답 코드도 함께 출력
                    Log.d("Response Code", "Response code: ${response.code()}")
                    Log.d("Response Body", "Response body: ${response.body()?.toString()}")

                }

                // 성공적으로 챌린지가 생성된 경우 참여 상태 저장
                saveChallengeParticipation(this@ChallengeCreateGroupPeopleActivity, category, true)

            }

            // challengeId를 SharedPreferences에 저장하는 함수
            private fun saveChallengeIdToPreferences(challengeId: Long) {
                val sharedPref = getSharedPreferences("ChallengePreferences", Context.MODE_PRIVATE)
                sharedPref.edit().putLong("challengeId", challengeId).apply()
            }

            override fun onFailure(call: Call<CreateChallengeResponse>, t: Throwable) {
                showToast("오류 발생: ${t.message}")
            }
        })
    }



    private fun saveChallengeParticipation(context: Context, category: String, isParticipating: Boolean) {
        val sharedPref = context.getSharedPreferences("ChallengePreferences", Context.MODE_PRIVATE)
        val challengeId = sharedPref.getLong("challengeId", -1L)  // 저장된 challengeId를 가져옴, 없으면 -1L 반환
        Log.d("HomeActivity", "저장된 challengeId: $challengeId")  // 로그로 값 확인
        sharedPref.edit().putBoolean("isParticipatingIn${category}", isParticipating).apply()
    }



    private fun getAccessToken(context: Context): String? {
        val sharedPref = context.getSharedPreferences("saved_user_info", Context.MODE_PRIVATE)
        return sharedPref.getString("accessToken", null)
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}