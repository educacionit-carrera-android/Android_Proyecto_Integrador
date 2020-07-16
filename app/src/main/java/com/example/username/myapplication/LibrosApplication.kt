package com.example.username.myapplication

import android.app.Application
import com.google.android.gms.ads.MobileAds

class LibrosApplication : Application() {

    companion object {
        lateinit var instance: LibrosApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        MobileAds.initialize(this)
    }

}