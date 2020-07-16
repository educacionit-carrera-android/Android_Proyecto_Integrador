package com.example.username.myapplication.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.username.myapplication.MainActivity
import com.example.username.myapplication.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class LibrosMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val body: String? = remoteMessage.notification?.body
        sendNotification(body)
    }

    private fun sendNotification(body: String?) {
        val notificationBuilder = NotificationCompat.Builder(this, getString(R.string.push_notif_channel_id))
                .setSmallIcon(android.R.drawable.ic_popup_reminder)
                .setContentTitle("Mis Libros")
                .setContentText(body)
                .setAutoCancel(true)
                .setContentIntent(getPendingIntent())

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(notificationManager)
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun getPendingIntent(): PendingIntent {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        return PendingIntent.getActivity(this, REQUEST_CODE, intent,
                PendingIntent.FLAG_ONE_SHOT)
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(getString(R.string.push_notif_channel_id),
                    "Push notifications",
                    NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private companion object {
        private const val REQUEST_CODE = 0
        private const val NOTIFICATION_ID = 0
    }

}