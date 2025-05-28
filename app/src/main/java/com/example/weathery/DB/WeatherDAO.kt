package com.example.weathery.Model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.weathery.AlarmAlert.Model.AlertItem
import com.example.weathery.Favorite.Model.FavoriteCity
import com.example.weathery.Home.Model.ForecastItemEntity

@Dao
interface WeatherDao {
    @Insert
    suspend fun insertAll(forecasts: List<ForecastItemEntity>)

    @Query("SELECT * FROM forecast_items")
    suspend fun getAllForecasts(): List<ForecastItemEntity>

    @Query("SELECT * FROM forecast_items WHERE cityName = :name")
    suspend fun getForecastsWithName(name: String): List<ForecastItemEntity>

    @Query("""
    SELECT * FROM forecast_items 
    WHERE latitude BETWEEN :lat - :delta AND :lat + :delta
    AND longitude BETWEEN :lon - :delta AND :lon + :delta
""")
    suspend fun getForecastsWithLatLong(lat: Double, lon: Double, delta: Double = 0.0001): List<ForecastItemEntity>

    @Query("DELETE FROM forecast_items")
    suspend fun clearAll()

    @Query("DELETE FROM forecast_items WHERE latitude = :lat AND longitude = :lon")
    suspend fun clear(lat: Double, lon: Double)

    @Query("""
        DELETE FROM forecast_items 
        WHERE dateTime < :todayStart 
        AND dateTime > :todayStart+3600000
        AND latitude = :lat 
        AND longitude = :lon
    """)
        suspend fun deleteOldForecasts(todayStart: Long, lat: Double, lon: Double)

    //--------------------------------------------------------------

    @Insert
    suspend fun insertFavorite(fav_city : FavoriteCity)

    @Query("SELECT * FROM FavoriteCity")
    suspend fun getAllFavCities(): List<FavoriteCity>

    @Delete
    suspend fun delete_fav_city(fav_city : FavoriteCity)

    //--------------------------------------------------------------

    @Insert
    suspend fun insertAlert(alert: AlertItem): Long

    @Query("SELECT * FROM AlertTable")
    suspend fun getAllAlerts(): List<AlertItem>

    @Delete
    suspend fun deleteAlert(alertItem: AlertItem)

    @Query("DELETE FROM AlertTable WHERE id = :id")
    suspend fun deleteAlertById(id:Int)
}