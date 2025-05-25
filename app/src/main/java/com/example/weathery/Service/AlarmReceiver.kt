package com.example.weathery.Service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startForegroundService

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val serviceIntent = Intent(context, AlarmService::class.java).apply {
            intent?.getStringExtra("message")?.let {
                putExtra("message", it)
            }
        }
        startForegroundService(context!!, serviceIntent)
        Log.d("AlarmService", "Call BroadcastReceiver")

    }

}
