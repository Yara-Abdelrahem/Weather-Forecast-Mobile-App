package com.example.weathery.Service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.weathery.Service.AlarmService

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra("message") ?: "Alarm!"
        val serviceIntent = Intent(context, AlarmService::class.java).apply {
            putExtra("message", message)
        }
        ContextCompat.startForegroundService(context!!, serviceIntent)
    }
}