package com.csit284.snakerancher

import android.content.Context
import android.graphics.Color
import android.graphics.Canvas
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.graphics.Paint
import android.view.MotionEvent

class SnakeGame(context: Context) : SurfaceView(context), SurfaceHolder.Callback {
    private var gameThread: Thread? = null
    private var running = false

    private var squareX = 100f  // Square's X position
    private var squareY = 100f  // Square's Y position
    private var direction = "RIGHT" // Default direction
    private val speed = 10f // Speed of movement

    private val paint = Paint().apply {
        color = Color.GREEN
        style = Paint.Style.FILL
    }

    init {
        holder.addCallback(this)
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        canvas.drawColor(Color.BLACK) // Clear screen
        canvas.drawRect(squareX, squareY, squareX + 50, squareY + 50, paint) // Draw square
    }

    fun update() {
        when (direction) {
            "LEFT" -> squareX -= speed
            "RIGHT" -> squareX += speed
            "UP" -> squareY -= speed
            "DOWN" -> squareY += speed
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                direction = when (direction) {
                    "RIGHT" -> "DOWN"
                    "DOWN" -> "LEFT"
                    "LEFT" -> "UP"
                    else -> "RIGHT"
                }
            }
        }
        return true
    }

    private fun startGameLoop() {
        running = true
        gameThread = Thread {
            while (running) {
                update()
                val canvas = holder.lockCanvas()
                if (canvas != null) {
                    synchronized(holder) {
                        draw(canvas)
                    }
                    holder.unlockCanvasAndPost(canvas)
                }
                Thread.sleep(50) // Adjust speed
            }
        }
        gameThread?.start()
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        startGameLoop()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        running = false
        gameThread?.join()
    }
}
