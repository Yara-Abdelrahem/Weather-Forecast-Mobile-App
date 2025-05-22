package com.example.weathery.Model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WeatherDao {
    @Insert
    suspend fun insertAll(forecasts: List<ForecastItemEntity>)

    @Query("SELECT * FROM forecast_items")
    suspend fun getAllForecasts(): List<ForecastItemEntity>

    @Query("DELETE FROM forecast_items")
    suspend fun clearAll()
}