package com.example.weathery.data

import com.example.weathery.Model.WeatherResponse
import com.example.weathery.network.IWeatherApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSource {
    companion object {
        private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    }

    private val weatherApiService: IWeatherApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IWeatherApiService::class.java)
    }

    suspend fun getHourlyForecast(lat: Double, lon: Double, apiKey: String): Result<WeatherResponse> {
        return try {
            val response = weatherApiService.getHourlyForecast(lat, lon, apiKey)
            if (response.code == "200") {
                Result.success(response)
            } else {
                Result.failure(Exception("API error: ${response.message}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}