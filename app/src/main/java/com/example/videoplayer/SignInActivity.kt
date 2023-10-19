package com.example.videoplayer

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.videoplayer.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth


class SignInActivity : AppCompatActivity() {


    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mSharedPrefs: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)




//        backbutton=findViewById(R.id.buttonback_signin)


        binding.buttonbackSignin.setOnClickListener {
            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
        }

//        newuserText=findViewById(R.id.newuser_tv)
        binding.newuserTv.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        binding.buttonsignin.setOnClickListener {
            val email = binding.editTextEmailAddressSignin.text.toString()
            val pass = binding.editTextPasswordSignin.text.toString()


            if (email.isNotEmpty() && pass.isNotEmpty()) {
                if(isValidEmail(email)){
                    firebaseAuth = FirebaseAuth.getInstance()
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val sm = SharedPrefManager(this@SignInActivity)
                        sm.setIsLoggedIn(true)
                        Toast.makeText(
                            this@SignInActivity,
                            "Successfully logged-in",
                            Toast.LENGTH_LONG
                        ).show()


                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()



                    } else {
                        Toast.makeText(this, it.exception?.message.toString(), Toast.LENGTH_SHORT).show()
                        Log.d(TAG, it.exception.toString())
                        Toast.makeText(this, "Something went wrong, Try Again", Toast.LENGTH_SHORT).show()

                    }
                }
            }
                else {
                    Toast.makeText(this, "Email is invalid!!", Toast.LENGTH_SHORT).show()

                }
            }
                else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()

            }
        }

    }
    fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

//    override fun onStart() {
//        super.onStart()
//
//        if(firebaseAuth.currentUser != null){
//            val intent = Intent(this, HomeActivity::class.java)
//            startActivity(intent)
//        }
//    }

}


