package com.example.weathery.AlarmAlert.Service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.weathery.R
import com.example.weathery.AlarmAlert.View.AlarmAlertActivity

class AlarmService : Service() {
    val channelId = "alarm_channel"
    private val alarmReceiver = AlarmReceiver()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("AlarmService", "onStartCommand called")
        val message = intent?.getStringExtra("message") ?: "Alarm!"
        val type = intent?.getStringExtra("type") ?: "notification"
        val id = intent?.getIntExtra("id", 0) ?: 0

        if (type == "alarm") {
            showNotification(message, id)
        }
        return START_NOT_STICKY
    }

    private fun showNotification(message: String, id: Int) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Alarm Channel",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for Alarm notifications"
                enableLights(true)
                enableVibration(true)
                setSound(
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM),
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
            }
            notificationManager.createNotificationChannel(channel)
        }

        val fullScreenIntent = Intent(this, AlarmAlertActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("message", message)
            putExtra("id", id)
        }
        val fullScreenPendingIntent = PendingIntent.getActivity(
            this, 0, fullScreenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Stop action intent
        val stopIntent = Intent(this, AlarmReceiver::class.java).apply {
            putExtra("id", id)
        }
        val stopPendingIntent = PendingIntent.getBroadcast(
            this, id, stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Weather Alert")
            .setContentText(message)
            .setSmallIcon(R.drawable.icon5)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .setAutoCancel(true)
            .addAction(R.drawable.ic_stop, "Stop", stopPendingIntent)
            .build()

        startForeground(id, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        alarmReceiver.stopAlarm()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}