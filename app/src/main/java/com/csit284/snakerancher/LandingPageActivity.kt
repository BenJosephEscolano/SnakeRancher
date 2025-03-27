package com.csit284.snakerancher

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button

class LandingPageActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.landingpage)

        // Initialize buttons
        val startButton = findViewById<Button>(R.id.button_start)
        val exitButton = findViewById<Button>(R.id.button_exit)

        // Set up start button click listener
        startButton.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }

        // Set up exit button click listener
        exitButton.setOnClickListener {
            finish() // Close the current activity
        }
    }
}
