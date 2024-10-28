package com.my.vitamateapp.mySupplement

import android.content.Context
import android.os.Bundle
import android.os.StrictMode
import android.util.Log // 로그를 위한 import 추가
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.android.identity.BuildConfig
import com.my.vitamateapp.Api.RetrofitInstance
import com.my.vitamateapp.Api.SupplementsSearchApi
import com.my.vitamateapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MySupplementActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_supplement)

        // StrictMode 설정
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAll() // 모든 쓰레드 관련 정책 감지
                    .penaltyLog() // 로그에 출력
                    .build()
            )
        }


        val searchIcon = findViewById<ImageView>(R.id.search_icon)
        val searchEditText = findViewById<EditText>(R.id.search_Supplement)

        searchIcon.setOnClickListener {
            val keyword = searchEditText.text.toString()
            if (keyword.isNotEmpty()) {
                searchSupplements(keyword)
            } else {
                Toast.makeText(this, "검색어를 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun searchSupplements(keyword: String) {
        // SharedPreferences에서 액세스 토큰 가져오기
        val sharedPreferences = getSharedPreferences("saved_user_info", Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("accessToken", null)

        if (accessToken.isNullOrEmpty()) {
            Toast.makeText(this, "액세스 토큰 없음", Toast.LENGTH_SHORT).show()
            Log.e("MySupplementActivity", "Access token is missing")
            return
        }

        Log.d("MySupplementActivity", "Access token found: $accessToken")
        Log.d("MySupplementActivity", "Searching supplements with keyword: $keyword")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Retrofit을 통해 API 호출 (액세스 토큰 포함)
                val response = RetrofitInstance.getInstance()
                    .create(SupplementsSearchApi::class.java)
                    .searchSupplements("Bearer $accessToken", "name", keyword) // Bearer 토큰 추가

                Log.d("MySupplementActivity", "API request sent")

                withContext(Dispatchers.Main) {
                    if (response.isSuccss) {
                        Log.d("MySupplementActivity", "API response success")
                        Log.d("MySupplementActivity", "Received supplements: ${response.result.supplementList}")

                        // 검색된 영양제 리스트가 비어있지 않은지 확인
                        if (response.result.supplementList.isNotEmpty()) {
                            // MySupplementActivity에서 데이터를 번들로 전달
                            val bundle = Bundle().apply {
                                putParcelableArrayList("supplements", ArrayList(response.result.supplementList))
                            }

                            val navHostFragment = supportFragmentManager
                                .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
                            val navController = navHostFragment.navController
                            navController.navigate(R.id.searchSupplementFragment, bundle)
                        } else {
                            Log.w("MySupplementActivity", "No supplements found")
                            Toast.makeText(this@MySupplementActivity, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.w("MySupplementActivity", "API response failure: ${response.message}")
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

}


