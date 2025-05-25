package com.example.weathery.ViewModel

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.weathery.Model.AlertItem
import com.example.weathery.Service.AlarmReceiver
        //Depends on  Alarm manager
class AlarmViewModel(private val context: Context) : ViewModel() {

    fun setAlarm(alarm_item: AlertItem) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Check permission for exact alarms (required on Android 12+)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                Log.e("AlarmViewModel", "Exact alarms are not permitted by the system.")
                return
            }
        }
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("message", alarm_item.msg)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                alarm_item.time,
                pendingIntent
            )
        } catch (e: SecurityException) {
            e.printStackTrace()
            Log.e("AlarmViewModel", "SecurityException: ${e.message}")
        }
    }
//    fun cancelAlarm(){
//        val pendingIntent = PendingIntent.getBroadcast(
//            context,
//            0,
//            AlarmReceiver::class.java,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//    }

}
