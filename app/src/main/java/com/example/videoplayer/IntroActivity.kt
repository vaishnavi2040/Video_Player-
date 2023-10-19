package com.example.videoplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import com.google.firebase.auth.FirebaseUser

class IntroActivity : AppCompatActivity() {
    lateinit var buttonsignup : Button
    lateinit var buttonsignin : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)




        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN,

            )




        buttonsignin=findViewById(R.id.button_signin_intro)
        buttonsignup=findViewById(R.id.button_signup_intro)
        buttonsignup.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
            finish()
        }

        buttonsignin.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }


    }
}