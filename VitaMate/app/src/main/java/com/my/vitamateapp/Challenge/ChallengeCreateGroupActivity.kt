package com.my.vitamateapp.Challenge


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.my.vitamateapp.ChallengeDTO.Category
import com.my.vitamateapp.R
import com.my.vitamateapp.databinding.ActivityChallengeCreateGroupBinding

class ChallengeCreateGroupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChallengeCreateGroupBinding
    private lateinit var category: Category // Add a variable to hold the category

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up Data Binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_challenge_create_group)

        // Retrieve the category from Intent and convert to Enum
        val categoryString = intent.getStringExtra("category")
        category = categoryString?.let { Category.valueOf(it) } ?: run {
            Toast.makeText(this, "카테고리를 선택하세요.", Toast.LENGTH_SHORT).show()
            finish() // Finish the activity if category is not found
            return
        }

        // Set up click listeners
        binding.preButton1.setOnClickListener {
            createClose1()
        }

        binding.nextButton.setOnClickListener {
            createGroupObjective()
        }

        binding.preButton2.setOnClickListener {
            createClose2()
        }
    }

    private fun createClose1() {
        finish()
    }

    private fun createGroupObjective() {
        val title = binding.title.text.toString()
        val description = binding.description.text.toString()
        val startDate = ""
        val duration = "ONE_WEEK"// startDate를 EditText에서 가져옵니다.

        if (title.isNotEmpty()) {

            val intent = Intent(this, ChallengeCreateGroupObjectiveActivity::class.java).apply {
                putExtra("category", category.name) // Send the category as a string
                putExtra("title", title)
                putExtra("description", description)
                putExtra("startDate", startDate)
                putExtra("duration", duration)
            }
            startActivity(intent)
        } else {
            Toast.makeText(this, "모든 필드를 입력해 주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createClose2() {
        val intent = Intent(this, ChallengeSelectModeActivity::class.java)
        startActivity(intent)
    }
}
