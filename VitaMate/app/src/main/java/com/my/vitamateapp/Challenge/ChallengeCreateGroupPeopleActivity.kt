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
import com.my.vitamateapp.ChallengeDTO.Category
import com.my.vitamateapp.ChallengeDTO.CreateChallengeRequest
import com.my.vitamateapp.ChallengeDTO.CreateChallengeResponse
import com.my.vitamateapp.ChallengeDTO.Duration
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.ActivityChallegneCreateGroupPeopleBinding
import com.my.vitamateapp.HomeActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        // createChallenge API 호출
        api.createChallenge("Bearer $accessToken", createChallengeRequest).enqueue(object : Callback<CreateChallengeResponse> {
            override fun onResponse(call: Call<CreateChallengeResponse>, response: Response<CreateChallengeResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()

                    // 응답이 성공적이고 result가 유효한지 확인
                    if (responseBody?.isSuccess == false) {
                        val challengeId = responseBody.result?.challengeId

                        // challengeId가 null이 아닌 경우 SharedPreferences에 저장하고 홈 화면으로 이동
                        if (challengeId != null) {
                            saveChallengeIdByCategory(this@ChallengeCreateGroupPeopleActivity, category, challengeId)
                            Log.d("SaveChallengeId", "Category: $category, ChallengeId: $challengeId, START_DATE: $startDate\")")

                            // 성공적으로 저장되었으면 홈 화면으로 이동
                            val intent = Intent(this@ChallengeCreateGroupPeopleActivity, HomeActivity::class.java).apply {
                                putExtra("challengeCreated", true) // 챌린지가 성공적으로 생성되었음을 알림
                                putExtra("message", "챌린지가 성공적으로 등록되었습니다.")
                                putExtra("startDate", startDate) // 디데이도 전달
                            }
                            startActivity(intent)
                            finish()
                        } else {
                            // challengeId가 null인 경우
                            showToast("챌린지 ID를 받아올 수 없습니다.")
                        }
                    } else {
                        // 응답이 성공적이지만 isSuccess가 false인 경우
                        val errorMessage = responseBody?.message ?: "챌린지 등록에 실패했습니다."
                        showToast(errorMessage)
                        Log.d("Error Response", "Message: $errorMessage")
                    }
                } else {
                    // HTTP 응답이 성공적이지 않은 경우
                    val errorMessage = response.errorBody()?.string() ?: "챌린지 등록에 실패했습니다."
                    showToast(errorMessage)
                    Log.d("Error", "Response Code: ${response.code()}, Error Message: $errorMessage")
                }
            }



            private fun saveChallengeIdByCategory(context: Context, category: String, challengeId: Long) {
                val sharedPref = context.getSharedPreferences("ChallengePrefs", Context.MODE_PRIVATE)
                sharedPref.edit().putLong("challengeId_$category", challengeId).apply()
            }

            private fun getChallengeIdByCategory(context: Context, category: String): Long {
                val sharedPref = context.getSharedPreferences("ChallengePrefs", Context.MODE_PRIVATE)
                val challengeId = sharedPref.getLong("challengeId_$category", -1L)
                Log.d("Challenge", "Retrieved challengeId for category $category: $challengeId")
                return challengeId
            }

            override fun onFailure(call: Call<CreateChallengeResponse>, t: Throwable) {
                showToast("오류 발생: ${t.message}")
            }
        })
    }



    private fun saveChallengeToPreferences(category: String, challengeId: Long) {
        val sharedPref = getSharedPreferences("ChallengePrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putLong("challengeId_$category", challengeId)
            apply()
        }
    }



    private fun getAccessToken(context: Context): String? {
        val sharedPref = context.getSharedPreferences("saved_user_info", Context.MODE_PRIVATE)
        return sharedPref.getString("accessToken", null)
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}