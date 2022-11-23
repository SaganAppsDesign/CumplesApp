package com.design.cumplepablo

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
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
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var goToMain: Intent
    private lateinit var inputMethodManager: InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize Firebase Auth
        auth = Firebase.auth

        val pref = getSharedPreferences("datos", MODE_PRIVATE)
        binding.etPersonName.setText(pref.getString("name", ""))
        binding.etPersonName.setText(pref.getString("birthday", ""))

        binding.btSiguiente.setOnClickListener{

            //Esconder teclado
            inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)

//            auth.signInAnonymously()
//                .addOnCompleteListener(this) { task ->
//                    if (task.isSuccessful) {
//                        // Sign in success, update UI with the signed-in user's information
//                        Log.i("SUCCESS authentication", "Authentication success.")
//                        val user = auth.currentUser
//                        //updateUI(user)
//                    } else {
//                        // If sign in fails, display a message to the user.
//                        Log.e("ERROR authentication", "Authentication failed.")
//                        Toast.makeText(baseContext, "Authentication failed.",
//                            Toast.LENGTH_SHORT).show()
//                        //updateUI(null)
//                    }
//                }
            name = binding.etPersonName.text.toString()
            birthday = binding.etBirthday.text.toString()

            if (birthday.isEmpty() || birthday.contains(".") || birthday.contains("/") || birthday.contains("*")
                || birthday.contains("-")|| birthday.contains("+")){
               Toast.makeText(this, "Fecha no válida", Toast.LENGTH_SHORT).show()
            } else {
                editor = pref.edit()
                editor.putString("name", name)
                editor.putString("birthday", birthday)
                editor.apply()
                finish()
                goToMain = Intent(this, MainActivity::class.java)
                startActivity(goToMain)
            }
        }
    }
}