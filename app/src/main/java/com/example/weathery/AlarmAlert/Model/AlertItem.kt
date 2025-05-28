package com.example.weathery.AlarmAlert.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AlertTable")
data class AlertItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val time: Long,
    val msg: String,
    val type: String
)