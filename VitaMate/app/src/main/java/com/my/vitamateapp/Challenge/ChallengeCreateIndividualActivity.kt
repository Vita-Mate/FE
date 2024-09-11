package com.my.vitamateapp.Challenge

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.ActivityChallengeCreateGroupBinding
import com.my.vitamateapp.databinding.ActivityChallengeCreateGroupObjectiveBinding
import com.my.vitamateapp.databinding.ActivityChallengeCreateIndividualBinding
import com.my.vitamateapp.registerPage.MainActivity

class ChallengeCreateIndividualActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChallengeCreateIndividualBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up Data Binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_challenge_create_individual)

        // Set up click listeners using binding
        binding.submitButton.setOnClickListener {
            createInd()
        }
    }

    private fun createInd() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("message", "챌린지가 등록되었습니다.")
        startActivity(intent)
    }

}
