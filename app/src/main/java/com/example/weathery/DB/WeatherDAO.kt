package com.example.weathery.Model

import androidx.room.Dao
import androidx.room.Delete
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

    //--------------------------------------------------------------

    @Insert
    suspend fun insertFavorite(fav_city : FavoriteCity)

    @Query("SELECT * FROM FavoriteCity")
    suspend fun getAllFavCities(): List<FavoriteCity>

    @Delete
    suspend fun delete_fav_city(fav_city : FavoriteCity)

    //--------------------------------------------------------------
    @Insert
    suspend fun insertAlert(alertItem: AlertItem)

    @Query("SELECT * FROM AlertTable")
    suspend fun getAllAlerts(): List<AlertItem>

    @Delete
    suspend fun deleteAlert(alertItem: AlertItem)
}