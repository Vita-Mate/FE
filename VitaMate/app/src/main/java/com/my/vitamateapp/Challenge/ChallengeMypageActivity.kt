package com.my.vitamateapp.Challenge

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.my.vitamateapp.R

class ChallengeMypageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.challenge_exercise_mypage1)

        val addMyRecordButton: ImageButton = findViewById(R.id.add_my_record)
        addMyRecordButton.setOnClickListener {
            showChallengeBottomSheetDialog2()
        }
    }

    private fun showChallengeBottomSheetDialog2() {
        val bottomSheetFragment = ChallengeBottomSheetDialog2Fragment()
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }

}