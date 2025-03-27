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

class ProfileActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)

        val button_ranking = findViewById<Button>(R.id.button_ranking)
        val imageview_exit = findViewById<ImageView>(R.id.imageview_exit)

        button_ranking.setOnClickListener() {
            val intent = Intent(this, LeaderboardActivity::class.java)
            startActivity(intent)
        }

        imageview_exit.setOnClickListener() {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
    }
}