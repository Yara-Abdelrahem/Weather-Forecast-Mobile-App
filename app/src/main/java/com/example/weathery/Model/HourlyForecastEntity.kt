package com.example.weathery

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hourly_forecasts")
data class HourlyForecastEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dateTime: Long, // Timestamp in seconds
    val temperature: Double, // In Celsius
    val description: String,
    val icon: String
)