package com.design.cumplepablo

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.design.cumplepablo.databinding.ActivityOnBoardingBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class OnBoarding : AppCompatActivity() {

    var name = ""
    var birthday = ""

    private lateinit var binding : ActivityOnBoardingBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize Firebase Auth
        auth = Firebase.auth

        val pref = getSharedPreferences("datos", MODE_PRIVATE)
        binding.etPersonName.setText(pref.getString("name", ""))

        binding.btSiguiente.setOnClickListener{

            auth.signInAnonymously()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.i("SUCCESS authentication", "Authentication success.")
                        val user = auth.currentUser
                        //updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.e("ERROR authentication", "Authentication failed.")
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                        //updateUI(null)
                    }
                }

            val editor: SharedPreferences.Editor = pref.edit()
            editor.putString("name", binding.etPersonName.text.toString())
            editor.putString("birthday", binding.etBirthday.text.toString())
            editor.apply()
            finish()
            name = binding.etPersonName.text.toString()
            birthday = binding.etBirthday.text.toString()
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("name", name)
            intent.putExtra("birthday", birthday)
            startActivity(intent)
        }
    }
}