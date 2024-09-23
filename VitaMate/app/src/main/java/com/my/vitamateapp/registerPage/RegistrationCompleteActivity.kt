package com.my.vitamateapp.registerPage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.kakao.sdk.user.UserApiClient
import com.my.vitamateapp.Api.RetrofitInstance
import com.my.vitamateapp.Api.SignUpApi
import com.my.vitamateapp.HomeActivity
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.ActivityRegistrationCompleteBinding
import com.my.vitamateapp.registerPage.SignUpRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegistrationCompleteActivity : AppCompatActivity() {
    private val api = RetrofitInstance.getInstance().create(SignUpApi::class.java)
    private lateinit var binding: ActivityRegistrationCompleteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registration_complete)

        val checkbox1 = binding.checkBox1
        val checkbox2 = binding.checkBox2
        val checkbox3 = binding.checkBox3
        val checkbox4 = binding.checkBox4
        val nextBtn = binding.nextBtn

        checkbox1.setOnCheckedChangeListener { _, _ -> checkIfAllChecked() }
        checkbox2.setOnCheckedChangeListener { _, _ -> checkIfAllChecked() }
        checkbox3.setOnCheckedChangeListener { _, _ -> checkIfAllChecked() }
        checkbox4.setOnCheckedChangeListener { _, _ -> checkIfAllChecked() }

        checkIfAllChecked()

        nextBtn.setOnClickListener {
            getEmailAndSignUp()
        }
    }

    private fun checkIfAllChecked() {
        val checkbox1 = binding.checkBox1
        val checkbox2 = binding.checkBox2
        val checkbox3 = binding.checkBox3
        val checkbox4 = binding.checkBox4
        val nextBtn = binding.nextBtn

        nextBtn.isEnabled = checkbox1.isChecked && checkbox2.isChecked && checkbox3.isChecked && checkbox4.isChecked
    }

    //카카오api로부터 사용자 이메일 가져오는 함수
    private fun getEmailAndSignUp() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("Kakao", "사용자 정보 요청 실패", error)
            } else if (user != null) {
                val email = user.kakaoAccount?.email
                if (email != null) {
                    signUpUser(email)
                } else {
                    Log.e("Kakao", "이메일을 가져올 수 없습니다.")
                }
            }
        }
    }

    //SignUpApi를 통해 회원가입 진행
    private fun signUpUser(email: String) {
        val sharedPreferences = getSharedPreferences("saved_user_info", Context.MODE_PRIVATE)
        val nickname = sharedPreferences.getString("nickname", "") ?: ""
        val birthDay = sharedPreferences.getString("birthDay", "") ?: ""
        val height = sharedPreferences.getInt("height", 0)
        val weight = sharedPreferences.getInt("weight", 0)
        val gender = sharedPreferences.getInt("gender", 0)

        // Log for debugging
        Log.d("SignUp", "회원가입 요청 시작")
        Log.d("SignUp", "회원가입 정보 - 이메일: $email, 닉네임: $nickname, 생년월일: $birthDay, 키: $height, 몸무게: $weight, 성별: $gender")

        if (email.isEmpty() || nickname.isEmpty() || birthDay.isEmpty() || height == 0 || weight == 0 || gender == 0) {
            Log.e("SignUp", "회원가입 필수 정보가 부족합니다.")
            return
        }

        val signUpRequest = SignUpRequest(
            email = email,
            nickname = nickname,
            birthDay = birthDay,
            height = height,
            weight = weight,
            gender = gender,
            bmr = sharedPreferences.getInt("bmr", 0).takeIf { it > 0 }
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val signUpResponse = api.signUp(signUpRequest)
                withContext(Dispatchers.Main) {
                    if (signUpResponse.isSuccessful) {
                        Log.d("SignUp", "회원가입 성공: ${signUpResponse.body()}")
                        gotoHome()
                    } else {
                        Log.e("SignUp", "회원가입 실패: ${signUpResponse.errorBody()?.string()}")
                    }
                }
            } catch (e: Exception) {
                Log.e("SignUp", "회원가입 API 호출 중 오류 발생", e)
            }
        }
    }

    private fun gotoHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}
