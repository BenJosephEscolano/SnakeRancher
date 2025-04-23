package com.csit284.snakerancher


import android.app.Activity
import android.content.Intent
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
import com.csit284.snakerancher.util.SnakeShape
import com.csit284.snakerancher.util.SnakeStyle

class CustomizationActivity : Activity() {
    private lateinit var prefManager: PrefManager

    private var selectedShape: SnakeShape = SnakeShape.SHARP
    private val selectedColors = mutableListOf(Color.GREEN, Color.GREEN, Color.GREEN)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customization)

        prefManager = PrefManager(this)

        val shapeGroup = findViewById<RadioGroup>(R.id.shapeRadioGroup)
        val rounded = findViewById<RadioButton>(R.id.radioRounded)
        val sharp = findViewById<RadioButton>(R.id.radioSharp)

        shapeGroup.setOnCheckedChangeListener { _, checkedId ->
            selectedShape = if (checkedId == R.id.radioRounded) SnakeShape.ROUNDED else SnakeShape.SHARP
        }


        findViewById<Button>(R.id.saveCustomization).setOnClickListener {
            val style = SnakeStyle(selectedShape, selectedColors.toList())
            prefManager.saveSnakeStyle(style)
            Toast.makeText(this, "Style Saved!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
            return@setOnClickListener
        }

        // Optionally load existing preferences
        val existing = prefManager.getSnakeStyle()
        selectedShape = existing.shape
        selectedColors.clear()
        selectedColors.addAll(existing.colors)

        if (selectedShape == SnakeShape.ROUNDED) rounded.isChecked = true else sharp.isChecked = true
    }


}