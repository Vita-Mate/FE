package com.my.vitamateapp.mySupplement

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.android.identity.BuildConfig
import com.my.vitamateapp.Api.RetrofitInstance
import com.my.vitamateapp.Api.SupplementsSearchApi
import com.my.vitamateapp.HomeActivity
import com.my.vitamateapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MySupplementActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_supplement)

        // StrictMode 설정
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build()
            )
        }

        // NavController 초기화
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        // UI 설정
        setupUI()
    }

    private fun setupUI() {
        // 검색 아이콘 클릭 리스너
        val searchIcon = findViewById<ImageView>(R.id.search_icon)
        val searchEditText = findViewById<EditText>(R.id.search_Supplement)

        searchIcon.setOnClickListener {
            val keyword = searchEditText.text.toString()
            if (keyword.isNotEmpty()) {
                searchSupplements(keyword)
            }
        }

        // 이전 버튼 클릭 리스너
        val preButton = findViewById<ImageButton>(R.id.pre_button)
        preButton.setOnClickListener {
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()
            } else {
                navigateHome()
            }
        }
    }

    private fun searchSupplements(keyword: String) {
        val sharedPreferences = getSharedPreferences("saved_user_info", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("accessToken", null)

        if (accessToken.isNullOrEmpty()) {
            Toast.makeText(this, "액세스 토큰 없음", Toast.LENGTH_SHORT).show()
            Log.e("MySupplementActivity", "Access token is missing")
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.getInstance()
                    .create(SupplementsSearchApi::class.java)
                    .searchSupplements("Bearer $accessToken", "name", keyword)

                withContext(Dispatchers.Main) {
                    if (response.isSuccss) {
                        val supplementList = response.result.supplementList
                        if (supplementList.isNotEmpty()) {
                            val bundle = Bundle().apply {
                                putParcelableArrayList("supplements", ArrayList(supplementList))
                            }
                            navController.navigate(R.id.searchSupplementFragment, bundle)
                        } else {
                            Toast.makeText(this@MySupplementActivity, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@MySupplementActivity, "검색 실패: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("MySupplementActivity", "API 호출 실패: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MySupplementActivity, "API 호출 실패: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun navigateHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    // 뒤로 가기 버튼 처리
    override fun onBackPressed() {
        val currentFragment = navController.currentDestination?.id

        // 검색 프래그먼트나 추가된 영양제 프래그먼트에서 뒤로 가기 시 홈 화면으로 이동 비타
        if (currentFragment == R.id.addedSupplementsFragment) {
            navigateHome()
        } else {
            super.onBackPressed()
        }
    }
}




