package com.my.vitamateapp





import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.ActivityNavigator
import com.kakao.sdk.user.UserApiClient
import com.my.vitamateapp.Challenge.ChallengeActivity
import com.my.vitamateapp.Challenge.ChallengeCreateGroupActivity
import com.my.vitamateapp.Challenge.ChallengeMypageActivity

import com.my.vitamateapp.Challenge.ChallengeSelectModeActivity
import com.my.vitamateapp.databinding.ActivityHomeBinding
import com.my.vitamateapp.registerPage.MainActivity
import java.sql.Date
import java.util.Locale

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        binding.homeHealthBtn.setOnClickListener {
            challenge_exercise()
        }

        binding.homeDrunkBtn.setOnClickListener {
            challenge_no_drunk()
        }

        binding.homeSmokeBtn.setOnClickListener {
            challenge_no_smoke()
        }

        // 로그아웃 버튼 클릭 시 로그아웃
        binding.kakaoLogoutButton.setOnClickListener {
            vitamate_logout()
        }

        // 계정탈퇴 버튼 클릭 시 계정삭제
        binding.deleteAccountButton.setOnClickListener {
            deleteAccount()
        }
    }

    /* 함수 구현 */
    private fun challenge_exercise() {
        // 챌린지 생성 여부를 확인하는 변수 (예시로 사용, 실제로는 데이터베이스나 SharedPreferences로부터 값을 가져올 수 있습니다)
        val isChallengeCreated = true // 챌린지가 생성된 경우 true로 설정

        // 챌린지 시작 날짜 (예시로 2024년 9월 1일로 설정)
        val challengeStartDate = "2024-09-01"
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = sdf.format(java.util.Date())

        try {
            val challengeDate = sdf.parse(challengeStartDate)
            val currentDateObj = sdf.parse(currentDate)

            // 챌린지가 생성되지 않았을 경우 ChallengeSelectModeActivity로 이동
            if (!isChallengeCreated) {
                val intent = Intent(this, ChallengeSelectModeActivity::class.java)
                startActivity(intent)
            }
            // 챌린지가 생성되었고, 시작일 이전이면 알림창 표시
            else if (currentDateObj.before(challengeDate)) {
                showAlertDialog("챌린지 시작 전입니다.")
            }
            // 챌린지가 생성되었고, 시작일 이후면 챌린지 페이지로 이동
            else {
                val intent = Intent(this, ChallengeMypageActivity::class.java)
                startActivity(intent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun challenge_no_drunk() {
        // 챌린지 생성 여부를 확인하는 변수 (예시로 사용, 실제로는 데이터베이스나 SharedPreferences로부터 값을 가져올 수 있습니다)
        val isChallengeCreated = false // 챌린지가 생성된 경우 true로 설정

        // 챌린지 시작 날짜 (예시로 2024년 9월 1일로 설정)
        val challengeStartDate = "2024-09-01"
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = sdf.format(java.util.Date())

        try {
            val challengeDate = sdf.parse(challengeStartDate)
            val currentDateObj = sdf.parse(currentDate)

            // 챌린지가 생성되지 않았을 경우 ChallengeSelectModeActivity로 이동
            if (!isChallengeCreated) {
                val intent = Intent(this, ChallengeSelectModeActivity::class.java)
                startActivity(intent)
            }
            // 챌린지가 생성되었고, 시작일 이전이면 알림창 표시
            else if (currentDateObj.before(challengeDate)) {
                showAlertDialog("챌린지 시작 전입니다.")
            }
            // 챌린지가 생성되었고, 시작일 이후면 챌린지 페이지로 이동
            else {
                val intent = Intent(this, ChallengeMypageActivity::class.java)
                startActivity(intent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 금주 금연은 운동과 챌린지 마이페이지가 다름
    private fun challenge_no_smoke() {
        // 챌린지 생성 여부를 확인하는 변수 (예시로 사용, 실제로는 데이터베이스나 SharedPreferences로부터 값을 가져올 수 있습니다)
        val isChallengeCreated = false // 챌린지가 생성된 경우 true로 설정

        // 챌린지 시작 날짜 (예시로 2024년 9월 1일로 설정)
        val challengeStartDate = "2024-09-20"
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = sdf.format(java.util.Date())

        try {
            val challengeDate = sdf.parse(challengeStartDate)
            val currentDateObj = sdf.parse(currentDate)

            // 챌린지가 생성되지 않았을 경우 ChallengeSelectModeActivity로 이동
            if (!isChallengeCreated) {
                val intent = Intent(this, ChallengeSelectModeActivity::class.java)
                startActivity(intent)
            }
            // 챌린지가 생성되었고, 시작일 이전이면 알림창 표시
            else if (currentDateObj.before(challengeDate)) {
                showAlertDialog("챌린지 시작 전입니다.")
            }
            // 챌린지가 생성되었고, 시작일 이후면 챌린지 페이지로 이동
            else {
                val intent = Intent(this, ChallengeMypageActivity::class.java)
                startActivity(intent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 알림창을 띄우는 함수
    private fun showAlertDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("챌린지 시작 전")
        builder.setMessage(message)
        builder.setPositiveButton("확인") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    private fun vitamate_logout() {
        // 로그아웃
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Log.e(ContentValues.TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
            } else {
                Log.i(ContentValues.TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
                startActivity(Intent(this, MainActivity::class.java))
                finish() // 로그아웃 성공하면 다시 앱 초기 화면으로 이동.
            }
        }
    }

    private fun deleteAccount() {
        // 계정삭제
        UserApiClient.instance.unlink { error ->
            if (error != null) {
                Log.e(ContentValues.TAG, "연결 끊기 실패", error)
            } else {
                Log.i(ContentValues.TAG, "연결 끊기 성공. SDK에서 토큰 삭제 됨")
                startActivity(Intent(this, MainActivity::class.java))
                finish() // 계정 삭제 성공하면 다시 앱 초기 화면으로 이동.
            }
        }
    }

}