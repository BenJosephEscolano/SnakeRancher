package com.csit284.snakerancher

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.csit284.snakerancher.util.PrefManager

class ResetPasswordActivity : Activity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        val returnToLogin = findViewById<Button>(R.id.button_resetPassword)
        val username = findViewById<EditText>(R.id.edittext_username)
        val password = findViewById<EditText>(R.id.edittext_password)
        val cancel = findViewById<TextView>(R.id.cancel)

        // added back button
        val imageview_back = findViewById<ImageView>(R.id.image_back)
        imageview_back.setOnClickListener {
            finish()
        }



        cancel.setOnClickListener(){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        returnToLogin.setOnClickListener{
            if (username.text.isNullOrEmpty() || password.text.isNullOrEmpty()){
                Toast.makeText(this,"username and password must not be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val prefManager = PrefManager(this)
            val newPassword = password.text.toString()

            val success = prefManager.updatePassword(username.text.toString(), newPassword)
            if (success) {
                Toast.makeText(this, "Password updated successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to update password.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }


}
