package com.csit284.snakerancher.util

data class SnakeStyle(
    val shape: SnakeShape,
    val colors: List<Int> // List of 3 colors (Int = Color int)
)

enum class SnakeShape {
    ROUNDED,
    SHARP
}