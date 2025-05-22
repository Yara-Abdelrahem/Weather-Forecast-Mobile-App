package com.example.weathery.Model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.fasterxml.jackson.annotation.JsonProperty

data class Main_Class_Data(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val sea_level: Int,
    val grnd_level: Int,
    val humidity: Int,
    val temp_kf: Double
)

data class Clouds(
    val all: Int
)

data class Rain(
    @JsonProperty("3h")
    val _3h: Double
)

data class Sys(
    val pod: String
)

@Entity(
    tableName = "weather_conditions",
    foreignKeys = [ForeignKey(
        entity =weather_forecasts_list::class,
        parentColumns = ["dt"],
        childColumns = ["forecastDt"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Weather(
    @PrimaryKey(autoGenerate = true) val weatherId: Int = 0,
    val forecastDt: Int, // Links to List.dt
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)
data class Wind(
    val speed: Double,
    val deg: Int,
    val gust: Double
)