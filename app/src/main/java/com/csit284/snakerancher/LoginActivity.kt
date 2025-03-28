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
        val account = AccountManager.create();
        val profile = Profile();
        intent?.let{
            it.getStringExtra("username")?.let{ username->
                profile.username = username;
            }
            it.getStringExtra("password")?.let{ password->
                profile.password = password;
            }
            account.add(profile)
        }



        button_register.setOnClickListener {
            if (edittext_password.text.isNullOrEmpty() || edittext_username.text.isNullOrEmpty()){
                Toast.makeText(this,"username and password must not be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!account.validate(edittext_username.text.toString(), edittext_password.text.toString())){
                Toast.makeText(this, "invalid credentials", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            //Toast.makeText(this, "Credentials: $profile", Toast.LENGTH_LONG).show()
            /*if (!edittext_password.text.toString().equals(profile.password) || !edittext_username.text.toString().equals(profile.username)){
                Toast.makeText(this, "invalid credentials", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }*/
            val intent = Intent(this, GameActivity::class.java)
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