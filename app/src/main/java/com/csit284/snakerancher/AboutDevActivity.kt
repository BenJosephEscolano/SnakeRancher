package com.csit284.snakerancher

import android.app.Activity
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AboutDevActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_dev)

        // added back button
        val back = findViewById<ImageView>(R.id.image_back)
        back.setOnClickListener {
            finish()
        }
    }
}