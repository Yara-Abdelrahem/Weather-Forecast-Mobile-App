package com.example.weathery.AlarmAlert.Service;

import android.app.NotificationManager
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log

class AlarmDismissReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val id = intent?.getIntExtra("id", 0) ?: 0
        notificationManager.cancel(id)
        Log.i("AlarmDismissReceiver", "Alarm with ID $id dismissed")
        // Stop any alarm sound if needed (you might need to manage MediaPlayer globally or via a Service)
    }
}
