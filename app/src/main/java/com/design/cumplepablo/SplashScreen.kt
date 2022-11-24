package com.design.cumplepablo

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlin.collections.ArrayList

class SplashScreen : AppCompatActivity() {
    private var storage = Firebase.storage
    var name = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        //shared preferences
        val pref = getSharedPreferences("datos", MODE_PRIVATE)
        name = pref.getString("name", "").toString()

//        if (name.isNotEmpty()) {
//            activityMain(MainActivity::class.java)
//        } else {
//            activityOnboarding(OnBoarding::class.java)
//        }
        activityOnboarding(OnBoarding::class.java)

   }

    private fun activityOnboarding(activity: Class<OnBoarding>){
        Handler().postDelayed({
            val intent = Intent(this, activity)
            startActivity(intent)
            finish()
        }, 1500)
    }

    private fun activityMain(activity: Class<MainActivity>){
        Handler().postDelayed({
            val intent = Intent(this, activity)
            val pref = getSharedPreferences("datos", MODE_PRIVATE)
            name = pref.getString("name", "").toString()
            intent.putExtra("name", name)
            finish()
            startActivity(intent)
        }, 1500)
    }
}