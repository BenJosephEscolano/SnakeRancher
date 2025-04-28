package com.csit284.snakerancher

import android.app.Activity
import android.os.Bundle
import com.csit284.snakerancher.util.SnakeGame

class GameActivity : Activity() {
    private lateinit var game: SnakeGame

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        game = SnakeGame(this)
        setContentView(game)
    }
}