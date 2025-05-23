package com.example.weathery

import com.example.weathery.Model.ForecastItemEntity
import com.example.weathery.Model.WeatherDao
import com.example.weathery.Model.WeatherResponse
import com.example.weathery.data.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepository(
    private val dao: WeatherDao,
    private val remoteDataSource: RemoteDataSource
) {
    companion object {
        fun create(dao: WeatherDao): WeatherRepository {
            return WeatherRepository(dao, RemoteDataSource())
        }
    }

    suspend fun refreshForecast(lat: Double, lon: Double, apiKey: String): Result<WeatherResponse> {
        return remoteDataSource.getHourlyForecast(lat, lon, apiKey).also { result ->
            if (result.isSuccess) {
                result.getOrNull()?.let { response ->
                    val entities = response.weatherList.map { weatherData ->
                        ForecastItemEntity(
                            dateTime = weatherData.dateTime,
                            temp = weatherData.main.temp,
                            feelsLike = weatherData.main.feelsLike,
                            tempMin = weatherData.main.tempMin,
                            tempMax = weatherData.main.tempMax,
                            pressure = weatherData.main.pressure,
                            humidity = weatherData.main.humidity,
                            windSpeed = weatherData.wind.speed,
                            cloud = weatherData.clouds.all,
                            visibility = weatherData.visibility,
                            description = weatherData.weather.firstOrNull()?.description ?: "",
                            icon = weatherData.weather.firstOrNull()?.icon ?: "",
                            cityName = response.city.name
                        )
                    }
                    withContext(Dispatchers.IO) {
                        dao.clearAll()
                        dao.insertAll(entities)
                    }
                }
            }
        }
    }

    suspend fun getStoredForecasts(): List<ForecastItemEntity> {
        return withContext(Dispatchers.IO) {
            dao.getAllForecasts()
        }
    }
}