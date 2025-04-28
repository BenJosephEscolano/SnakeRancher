package com.csit284.snakerancher


import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.csit284.snakerancher.util.PrefManager
import com.csit284.snakerancher.util.SnakeStyle

class CustomizationActivity : Activity() {
    private lateinit var prefManager: PrefManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customization)

        prefManager = PrefManager(this)

        val snakeHead = findViewById<Button>(R.id.snakeHead)
        val snakeBody = findViewById<Button>(R.id.snakeBody)
        val snakeTail = findViewById<Button>(R.id.snakeTail)

        val colorBody = listOf(
            findViewById<Button>(R.id.btnRedBody),
            findViewById<Button>(R.id.btnGreenBody),
            findViewById<Button>(R.id.btnBlueBody),
            findViewById<Button>(R.id.btnBlackBody)
        )

        val colorHead = listOf(
            findViewById<Button>(R.id.btnRedHead),
            findViewById<Button>(R.id.btnGreenHead),
            findViewById<Button>(R.id.btnBlueHead),
            findViewById<Button>(R.id.btnBlackHead)
        )

        val colorTail = listOf(
            findViewById<Button>(R.id.btnRedTail),
            findViewById<Button>(R.id.btnGreenTail),
            findViewById<Button>(R.id.btnBlueTail),
            findViewById<Button>(R.id.btnBlackTail)
        )

        colorBody.forEach { button ->
            button.setOnClickListener {
                val color = (button.backgroundTintList ?: ColorStateList.valueOf(Color.GRAY)).defaultColor
                snakeBody.backgroundTintList = ColorStateList.valueOf(color)
            }
        }

        colorTail.forEach { button ->
            button.setOnClickListener {
                val color = (button.backgroundTintList ?: ColorStateList.valueOf(Color.GRAY)).defaultColor
                snakeTail.backgroundTintList = ColorStateList.valueOf(color)
            }
        }

        colorHead.forEach { button ->
            button.setOnClickListener {
                val color = (button.backgroundTintList ?: ColorStateList.valueOf(Color.GRAY)).defaultColor
                snakeHead.backgroundTintList = ColorStateList.valueOf(color)
            }
        }

        findViewById<Button>(R.id.saveCustomization).setOnClickListener {
            val headColor = snakeHead.backgroundTintList?.defaultColor ?: Color.GRAY
            val bodyColor = snakeBody.backgroundTintList?.defaultColor ?: Color.GRAY
            val tailColor = snakeTail.backgroundTintList?.defaultColor ?: Color.GRAY

            val selectedColors = listOf(headColor, bodyColor, tailColor)
            val style = SnakeStyle(selectedColors.toList())
            prefManager.saveSnakeStyle(style)
            Toast.makeText(this, "Style Saved!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
            return@setOnClickListener
        }

    }


}