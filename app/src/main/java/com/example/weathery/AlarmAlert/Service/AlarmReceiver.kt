package com.example.weathery.AlarmAlert.Service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.example.weathery.R
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.weathery.AlarmAlert.AlarmActivity
import com.example.weathery.View.AlarmAlertActivity
import com.example.weathery.WeatherDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onReceive(context: Context, intent: Intent) {
        val msg = intent.getStringExtra("message") ?: "Check Weather Now!"
        val type = intent.getStringExtra("type") ?: "notification"
        val id = intent.getIntExtra("id", 0)
        Log.d("AlarmReceiver", "Received intent with message: $msg, type: $type, id: $id")
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "weather_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Weather Alerts",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
        if (type == "alarm") {
            //alarm
                val alarmIntent = Intent(context, AlarmActivity::class.java).apply {
                    putExtra("message", msg)
                    putExtra("id", id)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }
                context.startActivity(alarmIntent)

        } else {
            // notification
            val notification = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.notification1)
                .setContentTitle("Weather Alert")
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build()
            notificationManager.notify(id, notification)
        }

        CoroutineScope(Dispatchers.IO).launch {
            val db = WeatherDatabase.getDatabase(context)
            db.weatherDao().deleteAlertById(id)
        }
    }

    fun stopAlarm() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}