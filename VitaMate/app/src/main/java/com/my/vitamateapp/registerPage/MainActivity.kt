package com.my.vitamateapp.registerPage

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.my.vitamateapp.HomeActivity
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // 로그인 전 토큰 정보 보기
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Log.e(TAG, "토큰 정보 보기 실패", error)

            }
            else if (tokenInfo != null) {
                Log.i(TAG, "토큰 정보 보기 성공" +
                        "\n회원번호: ${tokenInfo.id}" +
                        "\n만료시간: ${tokenInfo.expiresIn} 초")
                gotoHome()
            }
        }

        binding.kakaoLoginButton.setOnClickListener{
            loginWithKakao()
        }
    }


    //로그아웃 하고 다시 회원가입하지 않게 수정하기.



    private fun loginWithKakao(){

        // 카카오계정으로 로그인 공통 callback 구성
        // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
                // 로그인 성공 후 사용자 정보 요청
                UserApiClient.instance.me { user, error ->
                    if (error != null) {
                        Log.e(TAG, "사용자 정보 요청 실패", error)
                    } else if (user != null) {
                        Log.i(TAG, "사용자 정보 요청 성공" +
                                "\n회원번호: ${user.id}" +
                                "\n이메일: ${user.kakaoAccount?.email}")
                    }
                }
                gotoRegister()//회원정보입력화면으로 이동

            }
        }

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                } else if (token != null) {
                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                    gotoRegister()//회원정보입력화면으로 이동
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }


    /* 함수 구현 */

    private fun gotoRegister() {
        startActivity(Intent(this, RegisterActivity::class.java))
        finish() // 현재 액티비티 종료.. 회원가입 정보 입력 화면으로
    }

    private fun gotoHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish() // 현재 액티비티 종료.. HOME화면으로
    }

}




