package com.csit284.snakerancher

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.csit284.snakerancher.util.PrefManager

class LandingPageActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)

        // Initialize buttons
        val startButton = findViewById<Button>(R.id.button_start)
        val exitButton = findViewById<Button>(R.id.button_exit)

        // Set up start button click listener
        startButton.setOnClickListener {
            val prefManager = PrefManager(this)
            if (prefManager.getLoggedInUser() != null){
                val intent = Intent(this, MenuActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }

        }

        // Set up exit button click listener
        exitButton.setOnClickListener {
            finish() // Close the current activity
        }
    }
}
