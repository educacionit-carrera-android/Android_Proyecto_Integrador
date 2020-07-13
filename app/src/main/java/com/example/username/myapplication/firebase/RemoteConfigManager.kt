package com.example.username.myapplication.firebase

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

object RemoteConfigManager {

    const val ABOUT_ME_ENABLED = "ABOUT_ME_ENABLED"

    @JvmStatic
    val instance by lazy {
        FirebaseRemoteConfig.getInstance()
    }

    @JvmStatic
    fun isAboutMeEnabled() = instance.getLong(ABOUT_ME_ENABLED) == 1L

    private val defaultParameters = mapOf(
            ABOUT_ME_ENABLED to 0
    )

    @JvmStatic
    fun init() {
        val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3500) //Tiempo de intervalo en milisegundos
                .build()
        instance.apply {
            setConfigSettingsAsync(configSettings)
            setDefaultsAsync(defaultParameters)
        }
    }

}