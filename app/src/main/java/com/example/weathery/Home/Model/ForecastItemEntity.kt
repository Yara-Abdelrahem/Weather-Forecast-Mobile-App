package com.example.weathery.Home.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "forecast_items")
data class ForecastItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dateTime: Long,
    val temp: Double,
    val feelsLike: Double,
    val tempMin: Double,
    val tempMax: Double,
    val pressure: Int,
    val humidity: Int,
    val windSpeed: Double,
    val cloud: Int,
    val visibility: Int,
    val description: String,
    val icon: String,
    val cityName: String,
    val latitude: Double ,
    val longitude: Double
)