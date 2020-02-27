package com.learnprogramming.learningtasks

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginbtn = findViewById(R.id.login) as Button

        loginbtn.setOnClickListener{
            Log.d(TAG,"loginbtn Clicked")

            var textViewUsername = findViewById<TextInputEditText>(R.id.usernameInputBox)
            var textViewPassword = findViewById<TextInputEditText>(R.id.passwordInputBox)
            var userName = textViewUsername.text.toString()
            var password = textViewPassword.text.toString()

            if(userName != "" && password != "")
            {
                if(userName == "Testing" && password == "123456789"){
                    Log.d(TAG,"userName : $userName password $password")
                    Toast.makeText(this,"userName : $userName password $password",Toast.LENGTH_SHORT).show()
                    setContentView(R.layout.activity_main)
                }else{
                    Toast.makeText(this,"Username and password mismatch",Toast.LENGTH_SHORT).show()

                }
            }
            else
            {
                Toast.makeText(this,"Username and password Cannot be empty",Toast.LENGTH_SHORT).show()
            }
        }

    }
}
