package com.example.weathery.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Embedded
import androidx.room.TypeConverters

@Entity(tableName = "weather_forecasts")
@TypeConverters(WeatherTypeConverters::class)
data class weather_forecasts_list(
    @PrimaryKey val dt: Int,
    val cityId: Int, // Foreign key to City
    @Embedded val main: Main_Class_Data,
    val weather: ArrayList<Weather>,
    @Embedded val clouds: Clouds,
    @Embedded val wind: Wind,
    val visibility: Int,
    val pop: Double,
    @Embedded val rain: Rain?,
    @Embedded val sys: Sys,
    val dt_txt: String
)