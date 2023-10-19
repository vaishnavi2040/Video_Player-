package com.example.videoplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.example.videoplayer.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUp : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_sign_up)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.buttonBack.setOnClickListener {
            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
            finish()
        }



        binding.buttonSignup.setOnClickListener {
            val email = binding.editTextEmailAddress.text.toString()
            val pass = binding.editTextPassword.text.toString()
            val confirmPass = binding.editTextConfirmPassword.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if(isValidEmail(email)){
                if (pass == confirmPass) {

                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {

                            Toast.makeText(this, "SignUp success", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this, SignInActivity::class.java)

                            startActivity(intent)
                            finish()


                        } else {
                            print(it.exception.toString())
                            Log.d("error",it.exception?.message.toString())
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()

                        }
                    }
                } else {
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }}else{
                    Toast.makeText(this, "Email is invalid", Toast.LENGTH_SHORT).show()

                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()

            }
        }
    }
    fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


    }