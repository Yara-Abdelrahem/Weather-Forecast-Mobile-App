package com.example.weathery.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "forecast_items")
data class ForecastItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dateTime: Long,
    val temp: Double,
    val description: String,
    val icon: String,
    val cityName: String
)