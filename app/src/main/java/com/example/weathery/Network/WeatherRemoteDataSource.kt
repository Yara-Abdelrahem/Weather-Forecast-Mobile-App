//package com.example.weathery
//
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import retrofit2.http.GET
//import retrofit2.http.Query
//
//data class WeatherResponse(
//    val current: CurrentWeather,
//    val hourly: List<HourlyForecast>,
//    val daily: List<DailyForecast>,
//    val city: City
//)
//
//data class CurrentWeather(
//    val temp: Double,
//    val weather: List<WeatherCondition>
//)
//
//data class HourlyForecast(
//    val dt: Long,
//    val temp: Double,
//    val weather: List<WeatherCondition>
//)
//
//data class DailyForecast(
//    val dt: Long,
//    val temp: Temp,
//    val weather: List<WeatherCondition>
//)
//
//data class Temp(
//    val min: Double,
//    val max: Double
//)
//
//data class WeatherCondition(
//    val description: String,
//    val icon: String
//)
//
//data class City(val name: String)
//
//object WeatherRemoteDataSource {
//    private const val BASE_URL = "[invalid url, do not cite]"
//    private const val APP_ID = "9a3fd0649e44a6fb8826038be3aa48fc"
//
//    private val retrofit = Retrofit.Builder()
//        .baseUrl(BASE_URL)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//
//    private interface WeatherApi {
//        @GET("onecall")
//        suspend fun getWeatherData(
//            @Query("lat") lat: Double,
//            @Query("lon") lon: Double,
//            @Query("exclude") exclude: String = "minutely,alerts",
//            @Query("appid") appId: String = APP_ID,
//            @Query("units") units: String = "metric"
//        ): WeatherResponse
//    }
//
//    suspend fun getWeatherData(lat: Double, lon: Double): WeatherResponse {
//        return retrofit.create(WeatherApi::class.java).getWeatherData(lat, lon)
//    }
//}

package com.example.weathery.data

import com.example.weathery.Model.WeatherResponse
import com.example.weathery.network.IWeatherApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherRemoteDataSource {
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