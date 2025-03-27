package com.csit284.snakerancher

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MenuActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mainmenu)

        val button_leaderboard = findViewById<Button>(R.id.button_leaderboard)
        val imageview_user = findViewById<ImageView>(R.id.user_icon)

        button_leaderboard.setOnClickListener() {
            val intent = Intent(this, LeaderboardActivity::class.java)
            startActivity(intent)
        }

        imageview_user.setOnClickListener() {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}