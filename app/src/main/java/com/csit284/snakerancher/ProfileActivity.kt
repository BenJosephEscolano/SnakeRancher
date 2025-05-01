package com.csit284.snakerancher

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.csit284.snakerancher.util.PrefManager

class ProfileActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)

        val button_ranking = findViewById<Button>(R.id.button_ranking)
        val button_edit = findViewById<Button>(R.id.button_edit)
        val username = findViewById<TextView>(R.id.textview_username)
        val highscore = findViewById<TextView>(R.id.textview_score)
        val rank = findViewById<TextView>(R.id.textView_leaderboardrank)

        // added back button
        val imageview_back = findViewById<ImageView>(R.id.image_back)
        imageview_back.setOnClickListener {
            finish()
        }


        val prefManager = PrefManager(this) // Initialize PrefManager

// Retrieve the logged-in user
        val loggedInUser = prefManager.getLoggedInUser()

        if (loggedInUser != null) {
            // Use the logged-in user's data to update the profile screen
            // For example, display the user's username and score
            username.text = loggedInUser.username
            highscore.text = "High-score: ${loggedInUser.highScore.toString()}"

            val userRank = prefManager.getUserRank()
            rank.text = if (userRank != null) {
                "Rank: #$userRank"
            } else {
                "Rank: Unranked"
            }

            button_ranking.setOnClickListener() {
                val intent = Intent(this, LeaderboardActivity::class.java)
                startActivity(intent)
            }
            button_edit.setOnClickListener() {
                val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)


                val editText = EditText(this)
                editText.hint = "Enter new username"

                val dialog = AlertDialog.Builder(this)
                    .setTitle("Change Username")
                    .setView(editText)
                    .setPositiveButton("Save") { _, _ ->
                        val newUsername = editText.text.toString()
                        if (newUsername.isNotBlank()) {
                            prefManager.updateUsername(loggedInUser.username, newUsername)
                            Toast.makeText(this, "Username updated!", Toast.LENGTH_SHORT).show()

                            username.text = newUsername
                        } else {
                            Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    .setNegativeButton("Cancel", null)
                    .create()

                dialog.show()
            }

        }
    }
}