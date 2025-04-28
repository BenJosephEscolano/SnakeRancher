package com.csit284.snakerancher.util

data class User(
    var username: String,
    var password: String,
    var highScore: Int = 0
)