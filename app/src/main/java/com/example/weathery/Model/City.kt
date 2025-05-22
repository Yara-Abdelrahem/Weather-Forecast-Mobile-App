package com.example.weathery.Model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class City(
    @PrimaryKey val id: Int,
    val name: String,
    @Embedded(prefix = "coord_") val coord: Coord,
    val country: String,
    val population: Int,
    val timezone: Int,
    val sunrise: Int,
    val sunset: Int
)

data class Coord(
    val lat: Double,
    val lon: Double
)