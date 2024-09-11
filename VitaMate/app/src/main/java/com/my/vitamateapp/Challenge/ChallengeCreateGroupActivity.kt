package com.my.vitamateapp.Challenge

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.ActivityChallengeCreateGroupBinding

class ChallengeCreateGroupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChallengeCreateGroupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up Data Binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_challenge_create_group)

        // Set up click listeners using binding
        binding.nextButton.setOnClickListener {
            createGroupObjective()
        }
        binding.preButton.setOnClickListener {
            createClose()
        }
    }

    private fun createGroupObjective() {
        val intent = Intent(this, ChallengeCreateGroupObjectiveActivity::class.java)
        startActivity(intent)
    }

    private fun createClose() {
        val intent = Intent(this, ChallengeSelectModeActivity::class.java)
        startActivity(intent)
    }
}
