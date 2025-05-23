package com.example.weathery.Network

import com.example.weathery.network.IWeatherApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object WeatherRetrofitClient {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    private val json = Json { ignoreUnknownKeys = true }
    // Use Lazy bc the returned data is alot and it may affect the app
    @OptIn(ExperimentalSerializationApi::class)
    val weatherService : IWeatherApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                json.asConverterFactory("application/json".toMediaType())
            )
            .build()
            .create(IWeatherApiService::class.java)
    }
}
