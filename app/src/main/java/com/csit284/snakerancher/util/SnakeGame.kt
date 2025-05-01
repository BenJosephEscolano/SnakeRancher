package com.csit284.snakerancher.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.PointF
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.RectF
import android.graphics.Typeface
import android.media.SoundPool
import android.media.AudioAttributes
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.csit284.snakerancher.MenuActivity
import com.csit284.snakerancher.R
import extRemoveLast
import kotlin.math.abs
import kotlin.random.Random

class SnakeGame(context: Context) : SurfaceView(context), SurfaceHolder.Callback {
    private var gameThread: Thread? = null
    private var running = false

    private var direction = "RIGHT" // Default direction
    private val cellSize = 40f;
    private var food = PointF(280f, 280f)
    private var poisoned = PointF(200f,400f)

    private var touchStartX = 0f
    private var touchStartY = 0f
    private val swipeThreshold = 50

    private var score = 0;

    private val rawFoodBitmap = BitmapFactory.decodeResource(resources, R.drawable.apple)
    private val foodBitmap = Bitmap.createScaledBitmap(rawFoodBitmap,
        cellSize.toInt(), cellSize.toInt(), true)
    private val background = BitmapFactory.decodeResource(resources, R.drawable.grass_template2)
    private val snake = mutableListOf(SnakeSegment(120f, 120f, "RIGHT", "RIGHT"))

    // added game music
    private var soundPool: SoundPool
    private var eatSoundId: Int = 0
    private var gameOverSoundId: Int = 0
    private var sfxEnabled: Boolean = true

    private var stopMusicCallback: (() -> Unit)? = null
    private var onGameRestartCallback: (() -> Unit)? = null

    fun setOnGameOverStopMusic(callback: () -> Unit) {
        this.stopMusicCallback = callback
    }

    fun setOnRestartStartMusic(callback: () -> Unit) {
        onGameRestartCallback = callback
    }

    init {
        holder.addCallback(this)

        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        sfxEnabled = prefs.getBoolean("sfx_enabled", true)

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(4)
            .setAudioAttributes(audioAttributes)
            .build()

        eatSoundId = soundPool.load(context, R.raw.eat_sfx, 1)
        gameOverSoundId = soundPool.load(context, R.raw.game_over, 1)
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        val paint = Paint()
        canvas.drawColor(Color.BLACK) // Clear screen
        canvas.drawBitmap(background, 0f, 0f, null)

        drawSnake(canvas)

        // Draw Food (same as before)
        canvas.drawBitmap(foodBitmap, food.x, food.y, null)
        paint.colorFilter = PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(foodBitmap, poisoned.x, poisoned.y, paint)

        paint.color = Color.WHITE
        paint.colorFilter = null
        paint.textSize = 60f
        paint.typeface = Typeface.DEFAULT_BOLD

        // Draw the score
        canvas.drawText("Score: $score", 50f, 100f, paint)
    }

    private fun drawSnake(canvas: Canvas){
        val prefManager = PrefManager(context)
        val snakeStyle = prefManager.getSnakeStyle()
        val colors = snakeStyle.colors

        // Then when drawing each segment of the snake:
        val paint = Paint()
        for (i in snake.indices) {
            val segment = snake[i]
            val color = when (i) {
                0 -> colors[0] // Head
                snake.lastIndex -> colors[2] // Tail
                else -> colors[1] // Body
            }
            paint.color = color
            val left = segment.x
            val top = segment.y

            canvas.drawRect(left, top, left + cellSize, top + cellSize, paint)
        }
    }

    // added eat sfx
    fun update() {
        var newHead = SnakeSegment(snake[0].x, snake[0].y, direction, snake[0].direction)
        when (direction) {
            "LEFT" -> newHead.x -= cellSize
            "RIGHT" -> newHead.x += cellSize
            "UP" -> newHead.y -= cellSize
            "DOWN" -> newHead.y += cellSize
        }

        snake.add(0, newHead) // Add new head

        // Check if food is eaten
        val headRect = RectF(newHead.x, newHead.y, newHead.x + cellSize, newHead.y + cellSize)
        val foodRect = RectF(food.x, food.y, food.x + cellSize, food.y + cellSize)
        val poisonedRect = RectF(poisoned.x, poisoned.y, poisoned.x + cellSize, poisoned.y + cellSize)
        if (headRect.intersect(foodRect)) {
            food = generateRandomFoodPosition() // Generate new food
            poisoned = generateRandomFoodPosition()
            score += 50;
            // added eat sfx
            if (sfxEnabled) soundPool.play(eatSoundId, 1f, 1f, 1, 0, 1f)
            // Don't remove tail => snake grows
        } else {
            snake.removeAt(snake.size - 1) // Remove tail
        }
        if (headRect.intersect(poisonedRect)){
            running = false
        }

        // Collision with self
        for (i in 1 until snake.size) {
            if (snake[0].x == snake[i].x && snake[0].y == snake[i].y) {
                running = false // Game over
            }
        }

        // Collision with wall
        if (snake[0].x < 0 || snake[0].x > width - 50 ||
            snake[0].y < 0 || snake[0].y > height - 50
        ) {
            running = false // Game over
        }
        if (running == false) {
            onGameOver()
        }
    }

    private fun generateRandomFoodPosition(): PointF {
        val gridSize = 40
        val cols = width / gridSize
        val rows = height / gridSize

        var point: PointF
        var collision: Boolean

        do {
            val randomCol = (0 until cols).random()
            val randomRow = (0 until rows).random()
            point = PointF((randomCol * gridSize).toFloat(), (randomRow * gridSize).toFloat())

            collision = snake.any { segment ->
                segment.x == point.x && segment.y == point.y
            }
        } while (collision)

        return point
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                touchStartX = event.x
                touchStartY = event.y
            }

            MotionEvent.ACTION_UP -> {
                val dx = event.x - touchStartX
                val dy = event.y - touchStartY

                if (abs(dx) > abs(dy)) {
                    if (dx > swipeThreshold && direction != "LEFT") {
                        direction = "RIGHT"
                    } else if (dx < -swipeThreshold && direction != "RIGHT") {
                        direction = "LEFT"
                    }
                } else {
                    if (dy > swipeThreshold && direction != "UP") {
                        direction = "DOWN"
                    } else if (dy < -swipeThreshold && direction != "DOWN") {
                        direction = "UP"
                    }
                }
            }
        }
        return true
    }

    private fun abs(n: Float): Float {
        if (n < 0) {
            return n * -1;
        }
        return n;
    }

    // added game over sfx
    fun onGameOver() {
        // added game over sfx
        stopMusicCallback?.invoke() // stop music
        if (sfxEnabled) soundPool.play(gameOverSoundId, 1f, 1f, 1, 0, 1f)

        // Save score or anything else
        val prefManager = PrefManager(context)
        prefManager.getLoggedInUser()?.let { prefManager.updateScore(it.username, score) }
        // Use a Handler to switch back to the UI thread and show a dialog or switch Activity
        Handler(Looper.getMainLooper()).post {
            AlertDialog.Builder(context)
                .setTitle("Game Over")
                .setMessage("Your score: $score")
                .setCancelable(false)
                .setPositiveButton("Restart") { _, _ ->
                    onGameRestartCallback?.invoke() // Start music again
                    restartGame()
                }
                .setNegativeButton("Exit") { _, _ ->
                    context.startActivity(Intent(context, MenuActivity::class.java))
                }
                .show()
        }
    }

    private fun startGameLoop() {
        running = true
        gameThread = Thread {
            var lastTime = System.nanoTime()
            val nsPerUpdate = 1_000_000_000.0 / 10.0 // 10 updates per second

            var delta = 0.0

            while (running) {
                val now = System.nanoTime()
                delta += (now - lastTime) / nsPerUpdate
                lastTime = now

                var shouldRender = false

                while (delta >= 1) {
                    update()
                    delta--
                    shouldRender = true
                }

                if (shouldRender) {
                    val canvas = holder.lockCanvas()
                    if (canvas != null) {
                        synchronized(holder) {
                            draw(canvas)
                        }
                        holder.unlockCanvasAndPost(canvas)
                    }
                }

                // Give time back to CPU to prevent tight loop
                Thread.sleep(2)
            }
        }
        gameThread?.start()
    }

    private fun restartGame() {
        // Reset snake position to starting position
        snake.clear()
        snake.add(SnakeSegment(120f, 120f, "RIGHT", "RIGHT")) // Add head at center

        // Reset direction (assuming you store direction)
        direction = "RIGHT"// Or whatever your default direction is

        // Reset score
        score = 0

        // Clear any pending movement handlers
        handler.removeCallbacksAndMessages(null)

        // Reset food position
        food = PointF(300f, 300f)

        // Reset game speed if you have increasing difficulty

        // Start the game loop again
        startGameLoop()

    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        startGameLoop()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        running = false
        gameThread?.join()
        soundPool.release() // cleanup
    }
}
