package com.example.weathery.AlarmAlert.ViewModel

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModel
import com.example.weathery.AlarmAlert.Model.AlertItem
import com.example.weathery.AlarmAlert.Service.AlarmReceiver
import com.example.weathery.AlarmAlert.Service.AlarmService
import com.example.weathery.R
import com.example.weathery.WeatherDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AlarmViewModel(val context: Context) : ViewModel() {
    private val dao = WeatherDatabase.getDatabase(context).weatherDao()

    suspend fun setAlarm(alertItem: AlertItem): Int = withContext(Dispatchers.IO) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val intent = Intent(context, AlarmService::class.java).apply {
//            putExtra("message", alertItem.msg)
//            putExtra("type", alertItem.type)
//            putExtra("id", alertItem.id) // Placeholder ID
//        }
//        val pendingIntent = PendingIntent.getService(
//            context,
//            alertItem.id,
//            intent,
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE else PendingIntent.FLAG_UPDATE_CURRENT
//        )

        // Insert into database and get the generated ID
        val newId = dao.insertAlert(alertItem).toInt()
        val updatedIntent = Intent(context, AlarmService::class.java).apply {
            putExtra("message", alertItem.msg)
            putExtra("type", alertItem.type)
            putExtra("id", newId)
        }
        val updatedPendingIntent = PendingIntent.getService(
            context,
            newId,
            updatedIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE else PendingIntent.FLAG_UPDATE_CURRENT
        )

//        if (alertItem.type == "alarm") {
//            alarmManager.setExactAndAllowWhileIdle(
//                AlarmManager.RTC_WAKEUP,
//                alertItem.time,
//                updatedPendingIntent
//            )
//        } else { // notification
//            createNotificationChannel()
//            scheduleNotification(alertItem.time, newId, alertItem.msg)
//        }
// Create intent for AlarmReceiver
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("message", alertItem.msg)
            putExtra("type", alertItem.type)
            putExtra("id", newId)
        }

// Create pending intent for broadcast
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            newId,
            intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            else
                PendingIntent.FLAG_UPDATE_CURRENT
        )

// Schedule with AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alertItem.time,
            pendingIntent
        )

        newId
    }

    suspend fun getAllAlerts(): List<AlertItem> = withContext(Dispatchers.IO) {
        dao.getAllAlerts()
    }

    suspend fun deleteAlert(id: Int) = withContext(Dispatchers.IO) {
        dao.deleteAlertById(id)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Weather Alerts"
            val descriptionText = "Channel for weather alert notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("weather_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun scheduleNotification(time: Long, id: Int, msg: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(context, "weather_channel")
            .setSmallIcon(R.drawable.notification1)
            .setContentTitle("Weather Alert")
            .setContentText(msg)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmService::class.java).apply {
            putExtra("message", msg)
            putExtra("type", "notification")
            putExtra("id", id)
        }
        val pendingIntent = PendingIntent.getService(
            context,
            id,
            intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE else PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
    }
}