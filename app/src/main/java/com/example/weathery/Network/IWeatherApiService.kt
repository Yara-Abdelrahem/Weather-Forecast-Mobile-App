package com.example.weathery.network

import com.example.weathery.Model.WeatherResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// Network DTOs live in com.example.weathery.network.dto (not shown here)
//import com.example.weathery.network.dto.HourlyForecastResponse

interface IWeatherApiService {
    @GET("forecast")
    suspend fun getHourlyForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String
    ):WeatherResponse
}