package com.my.vitamateapp

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.my.vitamateapp.databinding.ActivityFoodSearchBinding
import com.my.vitamateapp.databinding.ActivityMainBinding

class FoodSearch : AppCompatActivity() {

    private lateinit var binding: ActivityFoodSearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_food_search)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_food_search)

        var toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        supportActionBar?.title = "음식 검색"

        // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }


    //툴바 관련 AndroidManifest.xml 코드 추가하기
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // 툴바 눌렀을 때의 동작
                val intent = Intent(this, HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}