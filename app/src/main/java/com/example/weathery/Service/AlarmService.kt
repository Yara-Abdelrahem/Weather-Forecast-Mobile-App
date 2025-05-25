package com.example.weathery.Service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import com.example.weathery.R
import androidx.core.app.NotificationCompat

class AlarmService : Service() {
    val channelId = "alarm_channel"

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("AlarmService", "onStartCommand called")
        val message = intent?.getStringExtra("message") ?: "Alarm!"
        showNotification(message)
        //stopSelf()
        return START_NOT_STICKY
    }

    private fun showNotification(message: String) {

        // Create channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Alarm", NotificationManager.IMPORTANCE_HIGH)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val notification_builder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Alarm Notification")
            .setContentText(message)
            .setSmallIcon(R.drawable.icon5)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        val notification = notification_builder.build()
        Log.d("AlarmService", "Showing notification with message: $message")
        startForeground(1, notification)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
