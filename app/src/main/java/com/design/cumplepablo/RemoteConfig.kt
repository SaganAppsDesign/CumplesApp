package com.design.cumplepablo

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

class RemoteConfig {

    private val remoteConfig = Firebase.remoteConfig

    companion object {
        private const val MIN_REFRESH_SECONDS = 0L
     }

     init {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.fetch(MIN_REFRESH_SECONDS)
        remoteConfig.fetchAndActivate()
    }

    fun getSplashTitle(): String {
        return remoteConfig.getString("title_splash_screen")
    }

}