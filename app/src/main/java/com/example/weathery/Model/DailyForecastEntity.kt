package com.example.weathery

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_forecasts")
data class DailyForecastEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dateTime: Long, // Timestamp in seconds
    val tempMin: Double, // In Celsius
    val tempMax: Double, // In Celsius
    val description: String,
    val icon: String
)