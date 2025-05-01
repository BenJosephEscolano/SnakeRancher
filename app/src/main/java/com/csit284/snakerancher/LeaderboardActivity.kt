package com.csit284.snakerancher

import LeaderboardAdapter
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.csit284.snakerancher.util.PlayerScore
import com.csit284.snakerancher.util.PrefManager

class LeaderboardActivity : Activity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.leaderboard)

        val recyclerView: RecyclerView = findViewById(R.id.leaderboardRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val prefManager = PrefManager(this)
        val userList = prefManager.getUserList().sortedByDescending { it.highScore }
        val adapter = LeaderboardAdapter(userList)
        recyclerView.adapter = adapter

        // added back button
        val imageview_back = findViewById<ImageView>(R.id.image_back)
        imageview_back.setOnClickListener {
            finish()
        }

    }


}