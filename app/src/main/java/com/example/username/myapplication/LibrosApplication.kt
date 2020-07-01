package com.example.username.myapplication

import android.app.Application

class LibrosApplication : Application() {

    companion object {
        lateinit var instance: LibrosApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}