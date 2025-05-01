package com.csit284.snakerancher

import android.app.Activity
import android.media.MediaPlayer
import android.os.Bundle
import com.csit284.snakerancher.util.SnakeGame

class GameActivity : Activity() {
    private lateinit var game: SnakeGame

    // added game music
    private var mediaPlayer: MediaPlayer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        game = SnakeGame(this)
        setContentView(game)

        // Get preferences
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val musicEnabled = prefs.getBoolean("music_enabled", true)

        // Set callback to stop music on game over
        game.setOnGameOverStopMusic {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        }

        // Restart music on game restart
        game.setOnRestartStartMusic {
            if (musicEnabled) {
                mediaPlayer = MediaPlayer.create(this, R.raw.game_music)
                mediaPlayer?.isLooping = true
                mediaPlayer?.start()
            }
        }

        if (musicEnabled) {
            mediaPlayer = MediaPlayer.create(this, R.raw.game_music)
            mediaPlayer?.isLooping = true
            mediaPlayer?.start()
        }
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer?.pause()
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}