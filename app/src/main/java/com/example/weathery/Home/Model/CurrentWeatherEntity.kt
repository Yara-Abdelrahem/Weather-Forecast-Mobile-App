package com.example.weathery

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_weather")
data class CurrentWeatherEntity(
    @PrimaryKey val id: Int = 1, // Single entry for current weather
    val cityName: String,
    val temperature: Double, // In Celsius
    val description: String,
    val icon: String
)