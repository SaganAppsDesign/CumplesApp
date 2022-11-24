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
import java.util.ArrayList


class OnBoarding : AppCompatActivity() {

    var name = ""
    var birthday = ""

    private lateinit var binding : ActivityOnBoardingBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var goToMain: Intent
    private lateinit var inputMethodManager: InputMethodManager
    private lateinit var yearList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize Firebase Auth
        auth = Firebase.auth


        yearList = intent.getStringArrayListExtra("yearList") as ArrayList<String>

        val pref = getSharedPreferences("datos", MODE_PRIVATE)
        binding.etPersonName.setText(pref.getString("name", ""))
        binding.etPersonName.setText(pref.getString("birthday", ""))

        binding.btOpenPhoto.setOnClickListener{
            val intent = Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_GET_CONTENT)

            startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)
        }

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
                goToMain = Intent(this, MainActivity::class.java)
                goToMain.putStringArrayListExtra("yearList", yearList)
                startActivity(goToMain)
                finish()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 111 && resultCode == RESULT_OK) {
            val selectedFile = data?.data // The URI with the location of the file
        }
    }
}