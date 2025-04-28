package com.csit284.snakerancher.util

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PrefManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveUserList(users: List<User>) {
        val json = gson.toJson(users)
        prefs.edit().putString("userList", json).apply()
    }

    fun getUserList(): List<User> {
        val json = prefs.getString("userList", "[]")
        val type = object : TypeToken<List<User>>() {}.type
        return gson.fromJson(json, type)
    }

    fun addUser(newUser: User): Boolean {
        val users = getUserList().toMutableList()
        if (users.any { it.username == newUser.username }) {
            return false // Username taken
        }
        users.add(newUser)
        saveUserList(users)
        return true
    }

    fun login(username: String, password: String): Boolean {
        val res = getUserList().any { it.username == username && it.password == password }
        if (res){
            setLoggedInUser(username)
        }
        return res
    }

    fun updateScore(username: String, newScore: Int) {
        val users = getUserList().toMutableList()
        val user = users.find { it.username == username }
        if (user != null && newScore > user.highScore) {
            user.highScore = newScore
            saveUserList(users)
        }
    }

    fun updateUsername(oldUsername: String, newUsername: String): Boolean {
        val users = getUserList().toMutableList()

        // Check if new username is already taken
        if (users.any { it.username == newUsername }) {
            return false
        }

        val user = users.find { it.username == oldUsername }
        if (user != null) {
            user.username = newUsername
            saveUserList(users)
            setLoggedInUser(newUsername) // Update the currently logged-in user reference
            return true
        }
        return false
    }

    fun updatePassword(username: String, newPassword: String): Boolean {
        val users = getUserList().toMutableList()
        val user = users.find { it.username == username }
        if (user != null) {
            user.password = newPassword
            saveUserList(users)
            return true
        }
        return false
    }

    fun getUserScore(username: String): Int {
        return getUserList().find { it.username == username }?.highScore ?: 0
    }

    fun getUserRank(): Int? {
        val loggedInUser = getLoggedInUser() ?: return null
        val leaderboard = getLeaderboard()

        leaderboard.forEachIndexed { index, user ->
            if (user.username == loggedInUser.username) {
                return index + 1 // Rank is index + 1 because list starts at 0
            }
        }
        return null // If user is not found in leaderboard
    }

    fun getLeaderboard(): List<User> {
        val users = getUserList() // Get all users
        val sortedUsers = users.sortedByDescending { it.highScore } // Sort by high score, descending

        return sortedUsers
    }

    fun setLoggedInUser(username: String) {
        prefs.edit().putString("logged_in_user", username).apply()
    }

    fun getLoggedInUser(): User? {
        val username = prefs.getString("logged_in_user", null) ?: return null
        val users = getUserList()
        return users.find { it.username == username }
    }

    fun logoutUser() {
        prefs.edit().remove("logged_in_user").apply()
    }

    fun saveSnakeStyle(style: SnakeStyle) {
        val editor = prefs.edit()

        style.colors.forEachIndexed { index, color ->
            editor.putInt("snake_color_$index", color)
        }
        editor.apply()
    }

    fun getSnakeStyle(): SnakeStyle {
        val colors = List(3) { index ->
            prefs.getInt("snake_color_$index", Color.GREEN) // default color
        }
        return SnakeStyle(colors)
    }

}