package com.example.weathery.Model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.lifecycle.LiveData
import androidx.room.OnConflictStrategy
import com.example.weathery.CurrentWeatherEntity
import com.example.weathery.DailyForecastEntity
import com.example.weathery.HourlyForecastEntity

//@Dao
//interface CurrentWeatherDao {
//    @Query("SELECT * FROM current_weather WHERE id = 1")
//    fun getCurrentWeather(): LiveData<CurrentWeatherEntity>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertCurrentWeather(weather: CurrentWeatherEntity)
//}
//
//@Dao
//interface HourlyForecastDao {
//    @Query("SELECT * FROM hourly_forecasts ORDER BY dateTime ASC")
//    fun getAllHourlyForecasts(): LiveData<List<HourlyForecastEntity>>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertHourlyForecasts(forecasts: List<HourlyForecastEntity>)
//
//    @Query("DELETE FROM hourly_forecasts")
//    suspend fun deleteAllHourlyForecasts()
//}
//
//@Dao
//interface DailyForecastDao {
//    @Query("SELECT * FROM daily_forecasts ORDER BY dateTime ASC")
//    fun getAllDailyForecasts(): LiveData<List<DailyForecastEntity>>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertDailyForecasts(forecasts: List<DailyForecastEntity>)
//
//    @Query("DELETE FROM daily_forecasts")
//    suspend fun deleteAllDailyForecasts()
//}

@Dao
interface WeatherDao {
    @Insert
    suspend fun insertAll(forecasts: List<ForecastItemEntity>)

    @Query("SELECT * FROM forecast_items")
    suspend fun getAllForecasts(): List<ForecastItemEntity>

    @Query("SELECT * FROM forecast_items WHERE cityName = :name")
    suspend fun getForecastsWithName(name: String): List<ForecastItemEntity>

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