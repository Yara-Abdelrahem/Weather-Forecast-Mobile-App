package com.example.weathery.View

import com.example.weathery.AlarmAlert.Model.AlertItem

interface IAlarmScheduler {
    fun schedule_alarm (alarm_item: AlertItem)
    fun cancel_alarm (alertItem: AlertItem)
}