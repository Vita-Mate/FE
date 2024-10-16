package com.my.vitamateapp.Challenge

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.ActivityChallengeCreateGroupBinding
import com.my.vitamateapp.databinding.ActivityChallengeCreateGroupObjectiveBinding

class ChallengeCreateGroupObjectiveActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChallengeCreateGroupObjectiveBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up Data Binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_challenge_create_group_objective)

        // Set up click listeners using binding
        binding.preButton1.setOnClickListener {
            goPre()
        }
        binding.groupPreButton.setOnClickListener {
            goGroupName()
        }
        binding.groupNextButton.setOnClickListener {
            createGroupPeople()
        }
    }

    private fun goPre() {
        finish()
    }

    private fun goGroupName() {
        val intent = Intent(this, ChallengeCreateGroupActivity::class.java)
        startActivity(intent)
    }

    private fun createGroupPeople() {
        val intent = Intent(this, ChallengeCreateGroupPeopleActivity::class.java)
        startActivity(intent)
    }
}
