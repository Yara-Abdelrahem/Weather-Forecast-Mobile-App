// AlarmReceiver.kt
package com.example.weathery.Service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("AlarmReceiver", "Stop pressed → stopping service")
        context.startForegroundService(Intent(context, AlarmService::class.java))
    }
}
