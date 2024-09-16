package com.my.vitamateapp.Challenge


import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.my.vitamateapp.R
import java.io.File
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

class ChallengeMypageActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.challenge_exercise_mypage1)

        val addMyRecordButton: ImageButton = findViewById(R.id.add_my_record)
        addMyRecordButton.setOnClickListener {
            showChallengeBottomSheetDialog()
        }

    }

    private fun showChallengeBottomSheetDialog() {
        val bottomSheetFragment = ChallengeBottomSheetDialogFragment()
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }

}

