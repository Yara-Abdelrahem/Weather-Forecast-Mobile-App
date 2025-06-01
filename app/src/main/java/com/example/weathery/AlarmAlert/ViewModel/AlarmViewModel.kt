package com.example.weathery.AlarmAlert.ViewModel

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weathery.AlarmAlert.Model.AlarmLocalDataSource
import com.example.weathery.AlarmAlert.Model.AlertItem
import com.example.weathery.AlarmAlert.Repository.AlarmRepository
import com.example.weathery.AlarmAlert.Service.AlarmReceiver
import com.example.weathery.WeatherDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AlarmViewModel(val context: Context) : ViewModel() {
    private val dao = WeatherDatabase.getDatabase(context).weatherDao()
    private val localDataSource = AlarmLocalDataSource(dao)
    private val repository = AlarmRepository(localDataSource)

    private val _alerts = MutableLiveData<List<AlertItem>>()
    val alerts: LiveData<List<AlertItem>> get() = _alerts

    suspend fun setAlarm(alertItem: AlertItem): Int = withContext(Dispatchers.IO) {
        var id =repository.setAlarm(context, alertItem)
        loadAlerts()
         id
    }

    suspend fun insertAlert(alert: AlertItem): Long = withContext(Dispatchers.IO) {
        repository.insertAlert(alert)
    }

    suspend fun getAllAlerts(): List<AlertItem> = withContext(Dispatchers.IO) {


        repository.getAllAlerts()
    }

    suspend fun deleteAlert(alertItem: AlertItem) = withContext(Dispatchers.IO) {
        repository.deleteAlert(alertItem)
    }

    suspend fun deleteAlertById(id: Int) = withContext(Dispatchers.IO) {
        repository.deleteAlertById(id)
        loadAlerts()
    }

    suspend fun getAlertById(id: Int): AlertItem? = withContext(Dispatchers.IO) {
        repository.getAlertById(id)
    }
    suspend fun loadAlerts() {
        val now = System.currentTimeMillis()
        val allAlerts = repository.getAllAlerts()
        _alerts.postValue(allAlerts.filter { it.time > now })
    }

    fun cancelScheduledAlarm(context: Context, alertItem: AlertItem) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alertItem.id,
            intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            else
                PendingIntent.FLAG_NO_CREATE
        )

        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }

}
