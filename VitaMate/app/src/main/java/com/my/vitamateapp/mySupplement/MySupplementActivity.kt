package com.my.vitamateapp.mySupplement

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.my.vitamateapp.R

class MySupplementActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_supplement)

        val searchIcon = findViewById<ImageView>(R.id.search_icon)

        // 돋보기 아이콘 클릭 이벤트 처리
        searchIcon.setOnClickListener {
            // 검색된 영양제 Fragment 전환
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
            val navController = navHostFragment.navController
            navController.navigate(R.id.searchSupplementFragment)
        }
    }
}


