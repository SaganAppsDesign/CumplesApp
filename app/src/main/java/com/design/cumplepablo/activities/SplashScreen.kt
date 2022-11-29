package com.design.cumplepablo.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.design.cumplepablo.R
import com.design.cumplepablo.databinding.ActivityDescriptionScreenBinding
import com.design.cumplepablo.databinding.ActivitySplashScreenBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

class SplashScreen : AppCompatActivity() {

    var name = ""
    private lateinit var binding : ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = getSharedPreferences("datos", MODE_PRIVATE)
        name = pref.getString("name", "").toString()

        activityOnboarding(OnBoarding::class.java)
        initRemoteConfig()
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

    private fun initRemoteConfig(){
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.activate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Log.d("remoteConfigSuccess", "Config params updated: $updated:${binding.textView.text}")
                } else {
                    Log.e("Failed remote config", "Failed remote config")
                }
                binding.textView.text =  remoteConfig.getString("title_splash_screen")
            }
    }
}