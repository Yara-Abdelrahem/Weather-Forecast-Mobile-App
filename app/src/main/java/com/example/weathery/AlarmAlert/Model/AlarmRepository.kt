package com.example.weathery.AlarmAlert.Repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.weathery.AlarmAlert.Model.AlarmLocalDataSource
import com.example.weathery.AlarmAlert.Model.AlertItem
import com.example.weathery.AlarmAlert.Service.AlarmReceiver

class AlarmRepository(private val localDataSource: AlarmLocalDataSource) {

    suspend fun setAlarm(context: Context, alertItem: AlertItem): Int {
        val id = localDataSource.insertAlert(alertItem).toInt()

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("id", id)
            putExtra("message", alertItem.msg)
            putExtra("type", alertItem.type)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context, id, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alertItem.time,
            pendingIntent
        )

        return id
    }

    suspend fun getAlertById(id: Int): AlertItem? {
        return localDataSource.getAlertById(id)
    }

    suspend fun deleteAlertById(id: Int) {
        localDataSource.deleteAlertById(id)
    }

    fun cancelScheduledAlarm(context: Context, alertItem: AlertItem) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, alertItem.id, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

    suspend fun getAllAlerts(): List<AlertItem> {
        return localDataSource.getAllAlerts()
    }

    suspend fun insertAlert(alertItem: AlertItem):Long{
        return localDataSource.insertAlert(alertItem)
    }
    suspend fun deleteAlert(alertItem: AlertItem) {
        localDataSource.deleteAlertById(alertItem.id)
    }

}
