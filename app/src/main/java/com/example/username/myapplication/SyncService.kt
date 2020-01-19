package com.example.username.myapplication

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.IBinder
import android.util.Log
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class SyncService : Service() {

    lateinit var executor: ScheduledExecutorService
    val executeSync = Runnable { SyncData(applicationContext).execute() }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        executor = Executors.newScheduledThreadPool(1)
        executor.schedule(executeSync, 1L, TimeUnit.MINUTES)

        return START_NOT_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    class SyncData(var context: Context) : AsyncTask<Unit, Unit, Unit>() {

        override fun doInBackground(vararg p0: Unit?) {
            val libros = LibroManager.getInstance(context).libros
            sendDataToServer(libros)
        }

    }

    companion object {
        fun sendDataToServer(libros: List<Libro>) {
            Log.i("SyncService", "Datos enviados al servidor")
        }
    }

    override fun onDestroy() {
        executor.shutdown()
        Log.i("SyncService", "Servicio detenido")
        super.onDestroy()
    }

}