package com.example.username.myapplication.firebase

import android.os.Bundle
import com.example.username.myapplication.LibrosApplication
import com.google.firebase.analytics.FirebaseAnalytics

object LoggingManager {

    private fun logEvent(key: String, params: Bundle? = null) {
        FirebaseAnalytics.getInstance(LibrosApplication.instance.applicationContext).logEvent(key, params)
    }

    @JvmStatic
    fun logInicioSesion(recordarDatos: Boolean) {
        val bundle = Bundle()
        bundle.putBoolean("recordar_datos", recordarDatos)
        logEvent("inicio_sesion", bundle)
    }

    @JvmStatic
    fun logLibroClicked(nombreLibro: String) {
        logEvent("click_libro", Bundle().apply {
            putString("nombre_libro", nombreLibro)
        })
    }

    @JvmStatic
    fun logAboutMeClicked() = logEvent("click_acerca_de")

}