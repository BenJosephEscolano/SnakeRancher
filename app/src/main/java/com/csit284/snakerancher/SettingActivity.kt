package com.csit284.snakerancher

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.csit284.snakerancher.util.PrefManager

class SettingActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val returnToMain = findViewById<Button>(R.id.btn_return)
        val aboutDev = findViewById<Button>(R.id.btn_about_dev)

        returnToMain.setOnClickListener {
            val prefManager = PrefManager(this)
            prefManager.logoutUser()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        aboutDev.setOnClickListener(){
            val intent = Intent(this, AboutDevActivity::class.java)
            startActivity(intent)
        }
    }
}