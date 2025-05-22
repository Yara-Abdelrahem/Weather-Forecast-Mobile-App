package com.example.weathery.Model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
@Dao
interface WeatherDao {
    @Query("SELECT * FROM cities")
    fun getAllCities(): List<City>

    @Query("SELECT * FROM weather_forecasts WHERE cityId = :cityId")
    fun getForecastsByCity(cityId: Int): List<weather_forecasts_list>

    @Insert
    fun insertCity(city: City)

    @Insert
    fun insertForecast(forecast: weather_forecasts_list)

    @Insert
    fun insertWeather(weather: Weather)

    @Query("DELETE FROM cities")
    fun deleteAllCities()

    @Query("DELETE FROM weather_forecasts")
    fun deleteAllForecasts()

    @Query("DELETE FROM weather_conditions")
    fun deleteAllWeather()
}