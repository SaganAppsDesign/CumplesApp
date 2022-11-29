package com.design.cumplepablo.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.design.cumplepablo.R

class SplashScreen : AppCompatActivity() {

    var name = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        //shared preferences
        val pref = getSharedPreferences("datos", MODE_PRIVATE)
        name = pref.getString("name", "").toString()

        activityOnboarding(OnBoarding::class.java)
   }

    private fun activityOnboarding(activity: Class<OnBoarding>){
        Handler().postDelayed({
            val intent = Intent(this, activity)
            val pref = getSharedPreferences("datos", MODE_PRIVATE)
            name = pref.getString("name", "").toString()
            intent.putExtra("name", name)
            startActivity(intent)
            finish()
        }, 2000)
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