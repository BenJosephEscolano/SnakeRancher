package com.csit284.snakerancher

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ProfileActivity : Activity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        //Placeholder

        val gotosettings = findViewById<TextView>(R.id.go_to_settings)
        val gotoLeaderBoard = findViewById<TextView>(R.id.go_to_leaderboard)
        gotosettings.setOnClickListener(){
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
        gotoLeaderBoard.setOnClickListener(){
            val intent = Intent(this, LeaderboardActivity::class.java)
            startActivity(intent)
        }
    }
}