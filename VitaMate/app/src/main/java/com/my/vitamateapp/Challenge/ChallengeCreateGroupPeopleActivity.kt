package com.my.vitamateapp.Challenge

import GroupInfo
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.ActivityChallegneCreateGroupPeopleBinding
import com.my.vitamateapp.registerPage.MainActivity

class ChallengeCreateGroupPeopleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChallegneCreateGroupPeopleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_challegne_create_group_people)


        // Set up click listeners
        binding.preButton1.setOnClickListener {
            goPre()
        }
        binding.groupPeoplePrebtn.setOnClickListener {
            goCreateGroup()
        }

        binding.groupSubmitButton.setOnClickListener {
            saveDataToDatabase()
        }
    }

    private fun goPre() {
        finish()
    }

    private fun goCreateGroup() {
        val intent = Intent(this, ChallengeCreateGroupObjectiveActivity::class.java)
        startActivity(intent)
    }



    private fun saveDataToDatabase() {
        val minPeople = binding.minChallGroupPeople.text.toString().toIntOrNull() ?: 0
        val maxPeople = binding.maxChallGroupPeople.text.toString().toIntOrNull() ?: 0

        val groupInfo = GroupInfo(minPeople = minPeople, maxPeople = maxPeople)

        //메인 화면으로 이동
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("message", "챌린지가 등록되었습니다.")
        startActivity(intent)

//        CoroutineScope(Dispatchers.IO).launch {
//            db.groupInfoDao().insertGroupInfo(groupInfo)
//            finish() // Close the activity after saving data
//        }
    }
}
