package com.csit284.snakerancher

//import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
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
            val isLoggedIn = prefManager.login(edittext_username.text.toString(), edittext_password.text.toString())
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



    }
}