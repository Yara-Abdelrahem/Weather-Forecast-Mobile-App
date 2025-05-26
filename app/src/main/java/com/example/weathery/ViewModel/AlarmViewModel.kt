package com.example.yourapp.viewmodel

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weathery.Model.AlertItem
import com.example.weathery.Service.AlarmReceiver

class AlarmViewModel(application: Application) : AndroidViewModel(application) {
    private val _allAlarms = MutableLiveData<List<AlertItem>>(emptyList())
    val allAlarms: LiveData<List<AlertItem>> = _allAlarms

    private val alarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val context = application.applicationContext
    private lateinit var intent: Intent
    fun setAlarm(alarmData: AlertItem) {
         intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("message", alarmData.msg)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmData.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Schedule exact alarm
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmData.time, pendingIntent)

        // Update the list of alarms
        val currentAlarms = _allAlarms.value.orEmpty().toMutableList()
        currentAlarms.add(alarmData)
        _allAlarms.value = currentAlarms
    }
    fun cancelAlarm(){
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
}
}