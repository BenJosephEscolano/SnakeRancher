package com.csit284.snakerancher

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.PointF
import android.graphics.RectF
import android.graphics.Typeface
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.csit284.snakerancher.util.PrefManager
import com.csit284.snakerancher.util.SnakeShape


class SnakeGame(context: Context) : SurfaceView(context), SurfaceHolder.Callback {
    private var gameThread: Thread? = null
    private var running = false

    private var squareX = 100f  // Square's X position
    private var squareY = 100f  // Square's Y position
    private var direction = "RIGHT" // Default direction
    private val speed = 10f // Speed of movement
    private val cellSize = 40f;
    private var food = PointF(300f, 300f)

    private var touchStartX = 0f
    private var touchStartY = 0f
    private val swipeThreshold = 50

    private var score = 0;

    private val headUp = BitmapFactory.decodeResource(resources, R.drawable.head_up)
    private val headDown = BitmapFactory.decodeResource(resources, R.drawable.head_down)
    private val headLeft = BitmapFactory.decodeResource(resources, R.drawable.head_left)
    private val headRight = BitmapFactory.decodeResource(resources, R.drawable.head_right)

    private val bodyHorizontal = BitmapFactory.decodeResource(resources, R.drawable.body_horizontal)
    private val bodyVertical = BitmapFactory.decodeResource(resources, R.drawable.body_vertical)

    private val bodyCornerBL = BitmapFactory.decodeResource(resources, R.drawable.body_topleft)
    private val bodyCornerBR = BitmapFactory.decodeResource(resources, R.drawable.body_topright)
    private val bodyCornerTL = BitmapFactory.decodeResource(resources, R.drawable.body_bottomleft)
    private val bodyCornerTR = BitmapFactory.decodeResource(resources, R.drawable.body_bottomright)

    private val tailUp = BitmapFactory.decodeResource(resources, R.drawable.tail_up)
    private val tailDown = BitmapFactory.decodeResource(resources, R.drawable.tail_down)
    private val tailLeft = BitmapFactory.decodeResource(resources, R.drawable.tail_left)
    private val tailRight = BitmapFactory.decodeResource(resources, R.drawable.tail_right)
    private val foodBitmap = BitmapFactory.decodeResource(resources, R.drawable.apple)

    private val background = BitmapFactory.decodeResource(resources, R.drawable.grass_template2)

    // Add direction info for body parts
    data class SnakeSegment(var x: Float, var y: Float, val direction: String, val prevDirection: String)

    private val snake = mutableListOf(SnakeSegment(100f, 100f, "RIGHT", "RIGHT"))

    private val paint = Paint().apply {
        color = Color.GREEN
        style = Paint.Style.FILL
    }

    init {
        holder.addCallback(this)
    }


    private val gridPaint = Paint().apply {
        color = Color.GRAY
        style = Paint.Style.STROKE
        strokeWidth = 2f
    }


    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        canvas.drawColor(Color.BLACK) // Clear screen
        canvas.drawBitmap(background, 0f,0f, null)
//        val gridSize = 40 // Desired grid size (adjust this as necessary)
//        val cols = (width / gridSize) // Calculate number of columns
//        val rows = (height / gridSize) // Calculate number of rows

//        // Draw the vertical lines
//        for (col in 0 until cols) {
//            val x = col * gridSize.toFloat()
//            canvas.drawLine(x, 0f, x, height.toFloat(), gridPaint)
//        }
//
//        // Draw the horizontal lines
//        for (row in 0 until rows) {
//            val y = row * gridSize.toFloat()
//            canvas.drawLine(0f, y, width.toFloat(), y, gridPaint)
//        }
        val prefManager = PrefManager(context)
        val snakeStyle = prefManager.getSnakeStyle()
        val shape = snakeStyle.shape
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
            val left = segment.x * cellSize.toFloat()
            val top = segment.y * cellSize.toFloat()
            val centerX = left + cellSize / 2
            val centerY = top + cellSize / 2

            if (shape == SnakeShape.SHARP) {
                canvas.drawRect(left, top, left + cellSize, top + cellSize, paint)
            } else {
                val radius = cellSize / 2f
                canvas.drawCircle(centerX, centerY, radius, paint)
            }
        }
//        for (i in snake.indices) {
//            val segment = snake[i]
//            val bitmap = when {
//                i == 0 -> { // Head
//                    when (segment.direction) {
//                        "UP" -> headUp
//                        "DOWN" -> headDown
//                        "LEFT" -> headLeft
//                        "RIGHT" -> headRight
//                        else -> headRight // Default
//                    }
//                }
//                i == snake.size - 1 -> { // Tail
//                    when (segment.direction) {
//                        "UP" -> tailDown // Tail pointing upwards
//                        "DOWN" -> tailUp // Tail pointing downwards
//                        "LEFT" -> tailRight // Tail pointing left
//                        "RIGHT" -> tailLeft // Tail pointing right
//                        else -> tailRight // Default
//                    }
//                }
//                else -> { // Body
//                    val prevSegment = snake[i - 1]
//                    val nextSegment = snake[i + 1]
//                    when {
//                        prevSegment.direction == "UP" && nextSegment.direction == "RIGHT" -> bodyCornerTL
//                        prevSegment.direction == "UP" && nextSegment.direction == "LEFT" -> bodyCornerTR
//                        prevSegment.direction == "DOWN" && nextSegment.direction == "LEFT" -> bodyCornerBL
//                        prevSegment.direction == "DOWN" && nextSegment.direction == "RIGHT" -> bodyCornerBR
//                        prevSegment.direction == nextSegment.direction -> {
//                            if (prevSegment.direction == "LEFT" || prevSegment.direction == "RIGHT") {
//                                bodyHorizontal
//                            } else {
//                                bodyVertical
//                            }
//                        }
//                        else -> bodyHorizontal // Default to horizontal body
//                    }
//                }
//            }
//            canvas.drawBitmap(bitmap, segment.x, segment.y, null)
//        }

        // Draw Food (same as before)
        canvas.drawBitmap(foodBitmap, food.x, food.y, null)

        //val paint = Paint()
        paint.color = Color.WHITE
        paint.textSize = 60f
        paint.typeface = Typeface.DEFAULT_BOLD

        // Draw the score
        canvas.drawText("Score: $score", 50f, 100f, paint)
    }

    fun update() {
        var newHead = SnakeSegment(snake[0].x, snake[0].y, direction, snake[0].direction)
        when (direction) {
            "LEFT" -> newHead.x -= 60
            "RIGHT" -> newHead.x += 60
            "UP" -> newHead.y -= 60
            "DOWN" -> newHead.y += 60
        }
        for (i in 1 until snake.size) {
            val segment = snake[i]
            // Update the direction based on previous and next segment
        }

        snake.add(0, newHead) // Add new head

        // Check if food is eaten
        val headRect = RectF(newHead.x, newHead.y, newHead.x + 50, newHead.y + 50)
        val foodRect = RectF(food.x, food.y, food.x + 50, food.y + 50)
        if (headRect.intersect(foodRect)) {
            food = generateRandomFoodPosition() // Generate new food
            score += 50;
            // Don't remove tail => snake grows
        } else {
            snake.removeAt(snake.size - 1) // Remove tail
        }

        // Collision with self
        for (i in 1 until snake.size) {
            if (snake[0].x == snake[i].x && snake[0].y == snake[i].y) {
                running = false // Game over
            }
        }

        // Collision with wall
        if (snake[0].x < 0 || snake[0].x > width - 50 ||
            snake[0].y < 0 || snake[0].y > height - 50) {
            running = false // Game over
        }
        if (running == false){
            onGameOver()
        }
    }

    private fun generateRandomFoodPosition(): PointF {
        val gridSize = 50
        val cols = width / gridSize
        val rows = height / gridSize

        val randomCol = (0 until cols).random()
        val randomRow = (0 until rows).random()

        return PointF((randomCol * gridSize).toFloat(), (randomRow * gridSize).toFloat())
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

    private fun abs(n: Float): Float{
        if (n < 0){
            return n * -1;
        }
        return n;
    }

    fun onGameOver() {
        // Save score or anything else
        val prefManager = PrefManager(context)
        prefManager.getLoggedInUser()?.let { prefManager.updateScore(it.username,score) }
        // Use a Handler to switch back to the UI thread and show a dialog or switch Activity
        Handler(Looper.getMainLooper()).post {
            AlertDialog.Builder(context)
                .setTitle("Game Over")
                .setMessage("Your score: $score")
                .setCancelable(false)
                .setPositiveButton("Restart") { _, _ ->
                    restartGame()
                }
                .setNegativeButton("Exit") { _, _ ->
                    (context as Activity).finish()
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
        snake.add(SnakeSegment(100f, 100f, "RIGHT", "RIGHT")) // Add head at center

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
    }
}
