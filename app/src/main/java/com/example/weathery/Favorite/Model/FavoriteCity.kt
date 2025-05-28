package com.example.weathery.Favorite.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName ="FavoriteCity")
    data class FavoriteCity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var city_name : String ,
    var city_lat : Double,
    var city_lon: Double
)