package com.csit284.snakerancher

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
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
        // added confirm password
        val edittext_confirmpassword = findViewById<EditText>(R.id.edittext_confirmpassword)

        // added back button
        val imageview_back = findViewById<ImageView>(R.id.image_back)
        imageview_back.setOnClickListener {
            finish()
        }

        // added show password functionality
        val showPassword = findViewById<CheckBox>(R.id.checkbox_show_password)
        showPassword.setOnCheckedChangeListener { _, isChecked ->
            val inputType = if (isChecked) {
                android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            edittext_password.inputType = inputType
            edittext_confirmpassword.inputType = inputType

            // Move cursor to end
            edittext_password.setSelection(edittext_password.text.length)
            edittext_confirmpassword.setSelection(edittext_confirmpassword.text.length)
        }

        registerButton.setOnClickListener(){
            if (edittext_password.text.isNullOrEmpty() && edittext_username.text.isNullOrEmpty() && edittext_confirmpassword.text.isNullOrEmpty()){
                Toast.makeText(this,"username and password must not be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (edittext_password.text.isEmpty() || edittext_confirmpassword.text.isEmpty()) {
                Toast.makeText(this, "Password fields must not be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (edittext_password.text.toString() != edittext_confirmpassword.text.toString()) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
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