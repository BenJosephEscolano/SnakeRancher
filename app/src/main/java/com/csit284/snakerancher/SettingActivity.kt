package com.csit284.snakerancher

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Switch
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.csit284.snakerancher.util.PrefManager

class SettingActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val returnToMain = findViewById<Button>(R.id.button_log_out)
        val aboutDev = findViewById<Button>(R.id.button_about_dev)

        // added back button
        val imageview_back = findViewById<ImageView>(R.id.image_back)
        imageview_back.setOnClickListener {
            finish()
        }

        // added music/sfx toggles
        val musicSwitch = findViewById<Switch>(R.id.switch_music)
        val sfxSwitch = findViewById<Switch>(R.id.switch_sfx)

        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        musicSwitch.isChecked = prefs.getBoolean("music_enabled", true)
        sfxSwitch.isChecked = prefs.getBoolean("sfx_enabled", true)

        musicSwitch.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("music_enabled", isChecked).apply()
        }

        sfxSwitch.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("sfx_enabled", isChecked).apply()
        }

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