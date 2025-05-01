package com.csit284.snakerancher

//import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.csit284.snakerancher.util.PrefManager

class LoginActivity : Activity() {
    //@SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)


        val edittext_password = findViewById<EditText>(R.id.edittext_password)
        val edittext_username = findViewById<EditText>(R.id.edittext_username)
        val button_register = findViewById<Button>(R.id.login_button)
        val text_newAccount = findViewById<TextView>(R.id.textView_newAccount)
        val text_forgotPass = findViewById<TextView>(R.id.textview_forgotPass)

        // added back button
        val imageview_back = findViewById<ImageView>(R.id.image_back)
        imageview_back.setOnClickListener {
            finish()
        }

        // added show password functionality
        val showPassword = findViewById<CheckBox>(R.id.checkbox_show_password)
        showPassword.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                edittext_password.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                edittext_password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            edittext_password.setSelection(edittext_password.text.length)
        }


        intent?.let{
            it.getStringExtra("username")?.let{ username->
                edittext_username.setText(username)
            }
            it.getStringExtra("password")?.let{ password->
                edittext_password.setText(password)
            }
        }



        button_register.setOnClickListener {
            if (edittext_password.text.isNullOrEmpty() || edittext_username.text.isNullOrEmpty()){
                Toast.makeText(this,"username and password must not be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val prefManager = PrefManager(this)
            val isLoggedIn = prefManager.login(edittext_username.text.toString().trim(), edittext_password.text.toString().trim())
            if (!isLoggedIn){
                Toast.makeText(this, "invalid credentials", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)

        }

        text_forgotPass.setOnClickListener {
            val intent = Intent(this, ResetPasswordActivity::class.java)
            startActivity(intent)
        }

        text_newAccount.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        fun onBackPressed() {
            AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes") { _, _ -> finishAffinity() }
                .setNegativeButton("No", null)
                .show()
        }

    }
}