package com.example.weathery.AlarmAlert.Model

import com.example.weathery.AlarmAlert.Model.AlertItem
import com.example.weathery.Model.WeatherDao

class AlarmLocalDataSource(private val dao: WeatherDao) {

    suspend fun insertAlert(alert: AlertItem): Long = dao.insertAlert(alert)
    suspend fun getAllAlerts(): List<AlertItem> = dao.getAllAlerts()
    suspend fun deleteAlertById(id: Int) = dao.deleteAlertById(id)
    suspend fun getAlertById(id: Int): AlertItem? = dao.getAlertById(id)
}
