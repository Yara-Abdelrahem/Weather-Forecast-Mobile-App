package com.example.weathery.Home.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathery.Home.Model.ForecastItemEntity
import com.example.weathery.Home.Model.WeatherResponse
import com.example.weathery.WeatherRepository
import kotlinx.coroutines.launch
import java.time.ZoneId

class WeatherViewModel(private val repo: WeatherRepository) : ViewModel() {

    private val _forecasts = MutableLiveData<List<ForecastItemEntity>>()
    val forecasts: LiveData<List<ForecastItemEntity>> = _forecasts

    private val _hourlyForecasts = MutableLiveData<List<ForecastItemEntity>>()
    val hourlyForecasts: LiveData<List<ForecastItemEntity>> = _hourlyForecasts

    private val _dailySummaries = MutableLiveData<List<ForecastItemEntity>>()
    val dailySummaries: LiveData<List<ForecastItemEntity>> = _dailySummaries

    private val _weatherResponse = MutableLiveData<WeatherResponse?>()
    val weatherResponse: LiveData<WeatherResponse?> = _weatherResponse

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadForecasts(lat: Double, lon: Double, apiKey: String) {
        viewModelScope.launch {
            try {
                repo.refreshForecast(lat, lon, apiKey).fold(
                    onSuccess = { response ->
                        _weatherResponse.value = response
                        // Fetch stored forecasts
//                        val allForecasts = repo.getStoredForecasts()
                        val allForecasts = repo.getForecastsWithLatLong(lat, lon)
                        Log.d("WeatherViewModel", "Loaded ${allForecasts.size} forecasts for lat: $lat, lon: $lon")
                        // Split forecasts into today's hourly and next-days daily averages
                        val (hourlyToday, dailySummary) = repo.splitForecastItems(allForecasts, ZoneId.systemDefault())
                        _hourlyForecasts.value = hourlyToday
                        _dailySummaries.value = dailySummary
                        // Also keep full list if needed
                        _forecasts.value = allForecasts
                        _error.value = null
                    },
                    onFailure = { err ->
                        _error.value = err.message
                    }
                )
            } catch (e: Exception) {
                _error.value = e.localizedMessage
            }
        }
    }
}