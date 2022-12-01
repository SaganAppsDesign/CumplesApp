package com.design.cumplepablo.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.design.cumplepablo.RemoteConfig
import com.design.cumplepablo.databinding.ActivitySplashScreenBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

class SplashScreen : AppCompatActivity() {

    var name = ""
    private lateinit var binding : ActivitySplashScreenBinding
    private var remoteConfig = RemoteConfig()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textView.text = remoteConfig.getSplashTitle()

        val pref = getSharedPreferences("datos", MODE_PRIVATE)
        name = pref.getString("name", "").toString()

        Handler().postDelayed({
            activityOnboarding(OnBoarding::class.java)
        }, 1000)

   }

    private fun activityOnboarding(activity: Class<OnBoarding>){
        Handler().postDelayed({
            val intent = Intent(this, activity)
            val pref = getSharedPreferences("datos", MODE_PRIVATE)
            name = pref.getString("name", "").toString()
            intent.putExtra("name", name)
            startActivity(intent)
            finish()
        }, 2500)
    }
}