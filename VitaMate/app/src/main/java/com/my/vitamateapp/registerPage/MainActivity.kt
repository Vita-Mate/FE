package com.my.vitamateapp.registerPage

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.my.vitamateapp.Api.RetrofitInstance
import com.my.vitamateapp.Api.SignInApi
import com.my.vitamateapp.HomeActivity
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val api = RetrofitInstance.getInstance().create(SignInApi::class.java)
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // 카카오 로그인 상태 확인
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Log.e(TAG, "토큰 정보 확인 실패", error)
                // 토큰이 유효하지 않으므로 로그인 절차 진행
            } else if (tokenInfo != null) {
                Log.i(TAG, "토큰 정보 확인 성공: ${tokenInfo.id}")
                // 토큰이 유효하므로 사용자 등록 상태 확인
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        Log.e(TAG, "사용자 정보 요청 실패", error)
                    } else if (user != null) {
                        Log.i(TAG, "사용자 정보 요청 성공")
                        user.kakaoAccount?.email?.let { email ->
                            Log.i(TAG, "가져온 이메일: $email") // 이메일 확인 로그 추가
                            sendEmailToBackend(email) // 백엔드에 이메일을 전송하여 등록 상태 확인
                        }
                    }
                }
            }
        }

        // 카카오 로그인 버튼 클릭 리스너
        binding.kakaoLoginButton.setOnClickListener {
            loginWithKakao()
        }
    }

    private fun loginWithKakao() {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오 로그인 실패", error)
            } else if (token != null) {
                Log.i(TAG, "카카오 로그인 성공, 액세스 토큰: ${token.accessToken}") // 토큰 확인 로그 추가
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        Log.e(TAG, "사용자 정보 요청 실패", error)
                    } else if (user != null) {
                        Log.i(TAG, "사용자 정보 요청 성공")
                        user.kakaoAccount?.email?.let { email ->
                            Log.i(TAG, "가져온 이메일: $email") // 이메일 확인 로그 추가
                            sendEmailToBackend(email) // 백엔드에 이메일을 전송하여 등록 상태 확인
                        }
                    }
                }
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }

    private fun sendEmailToBackend(email: String) {
        Log.i(TAG, "백엔드로 이메일 전송: $email") // 백엔드 전송 전 로그 추가
        val request = SignInRequest(email)
        api.signIn(request).enqueue(object : Callback<SignInResponse> {
            override fun onResponse(call: Call<SignInResponse>, response: Response<SignInResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()?.result
                    val accessToken = result?.accessToken

                    // Access Token 및 회원가입 여부 확인 로직
                    if (!accessToken.isNullOrEmpty()) {
                        Log.i(TAG, "백엔드 응답 성공, Access Token: $accessToken") // 응답 성공 및 토큰 확인 로그 추가

                        // Access Token을 SharedPreferences에 저장
                        val sharedPref = getSharedPreferences("saved_user_info", Context.MODE_PRIVATE)
                        with(sharedPref.edit()) {
                            putString("accessToken", accessToken)
                            apply() // 변경 사항 저장
                        }

                        gotoHome() // 이미 등록된 사용자, 홈 화면으로 이동
                    } else {
                        Log.w(TAG, "Access Token이 없거나 등록되지 않은 사용자") // 경고 로그 추가
                        gotoRegister() // 등록되지 않은 사용자 또는 accessToken이 null인 경우, 회원가입 화면으로 이동
                    }
                } else {
                    val errorResponse = response.errorBody()?.string()
                    Log.e(TAG, "백엔드 응답 실패, 상태 코드: ${response.code()}, 오류 메시지: $errorResponse") // 오류 로그 추가
                    gotoRegister() // 실패 시에도 회원가입 화면으로 이동
                }
            }

            override fun onFailure(call: Call<SignInResponse>, t: Throwable) {
                Log.e(TAG, "API 요청 실패", t)
                gotoRegister() // API 요청 실패 시에도 회원가입 화면으로 이동
            }
        })
    }



    private fun gotoRegister() {
        Log.i(TAG, "회원가입 화면으로 이동") // 회원가입 화면 이동 로그 추가
        startActivity(Intent(this, RegisterActivity::class.java))
        finish() // 현재 액티비티 종료 후 회원가입 화면으로 이동
    }

    private fun gotoHome() {
        Log.i(TAG, "홈 화면으로 이동") // 홈 화면 이동 로그 추가
        startActivity(Intent(this, HomeActivity::class.java))
        finish() // 현재 액티비티 종료 후 홈 화면으로 이동
    }
}