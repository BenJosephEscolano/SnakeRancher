package com.csit284.snakerancher

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.csit284.snakerancher.util.PrefManager
import com.csit284.snakerancher.util.User

class RegisterActivity : Activity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)

        val registerButton = findViewById<Button>(R.id.btn_register)
        val edittext_password = findViewById<EditText>(R.id.edittext_password)
        val edittext_username = findViewById<EditText>(R.id.edittext_username)
        registerButton.setOnClickListener(){
            if (edittext_password.text.isNullOrEmpty() && edittext_username.text.isNullOrEmpty()){
                Toast.makeText(this,"username and password must not be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val prefManager = PrefManager(this)
            val isRegistered = prefManager.addUser(User(edittext_username.text.toString(), edittext_password.text.toString(), 0))
            if (isRegistered) {
                Toast.makeText(this, "User registered!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Username taken.", Toast.LENGTH_SHORT).show()
            }

            val intent = Intent(this, LoginActivity::class.java).apply{
                putExtra("username", edittext_username.text.toString());
                putExtra("password", edittext_password.text.toString())
            }
            startActivity(intent)
        }


    }
}