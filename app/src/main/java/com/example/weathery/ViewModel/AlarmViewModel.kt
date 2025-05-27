package com.example.weathery.ViewModel

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weathery.Model.AlertItem
import com.example.weathery.Service.*

class AlarmViewModel(application: Application) : AndroidViewModel(application) {
    private val _allAlarms = MutableLiveData<List<AlertItem>>(emptyList())
    val allAlarms: LiveData<List<AlertItem>> = _allAlarms

    private val alarmManager =
        application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val context = application.applicationContext

    /** Schedule a new alarm and add it to LiveData */
    fun setAlarm(alarmData: AlertItem) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = "com.example.weathery.ACTION_ALARM"
            putExtra("message", alarmData.msg)
            putExtra("alarmId", alarmData.id)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmData.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            alarmData.time,
            pendingIntent
        )

        _allAlarms.value = _allAlarms.value.orEmpty() + alarmData
    }

    /** Cancel an existing alarm by its ID and remove it from LiveData */
    fun cancelAlarm(alarmId: Int) {
        // Recreate the exact same PendingIntent
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = "com.example.weathery.ACTION_ALARM"
            putExtra("alarmId", alarmId)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Cancel it in AlarmManager
        alarmManager.cancel(pendingIntent)
        // Also cancel the PendingIntent so the system can reclaim it
        pendingIntent.cancel()

        // Remove from LiveData list
        _allAlarms.value = _allAlarms.value.orEmpty()
            .filterNot { it.id == alarmId }
    }
}
