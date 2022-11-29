package com.design.cumplepablo

import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

class RemoteConfig {

    private val remoteConfig = Firebase.remoteConfig

     init {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.fetch(0)
        remoteConfig.fetchAndActivate()
    }

    fun getSplashTitle(): String {
        return remoteConfig.getString("title_splash_screen")
    }

}