package com.example.weathery.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathery.Model.ForecastItemEntity
import com.example.weathery.Model.WeatherResponse
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
                        val allForecasts = repo.getStoredForecasts()
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


//package com.example.weathery.ViewModel
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.weathery.Model.ForecastItemEntity
//import com.example.weathery.Model.WeatherResponse
//import com.example.weathery.WeatherRepository
//import kotlinx.coroutines.launch
//
//class WeatherViewModel(private val repo: WeatherRepository) : ViewModel() {
//    private val _forecasts = MutableLiveData<List<ForecastItemEntity>>()
//    val forecasts: LiveData<List<ForecastItemEntity>> = _forecasts
//
//    private val _weatherResponse = MutableLiveData<WeatherResponse?>()
//    val weatherResponse: LiveData<WeatherResponse?> = _weatherResponse
//
//    private val _error = MutableLiveData<String?>()
//    val error: LiveData<String?> = _error
//
//    fun loadForecasts(lat: Double, lon: Double, apiKey: String) {
//        viewModelScope.launch {
//            try {
//                repo.refreshForecast(lat, lon, apiKey).fold(
//                    onSuccess = { response ->
//                        _weatherResponse.value = response
//                        _forecasts.value = repo.getStoredForecasts()
//                        _error.value = null
//                    },
//                    onFailure = { error ->
//                        _error.value = error.message
//                    }
//                )
//            } catch (e: Exception) {
//                _error.value = e.localizedMessage
//            }
//        }
//    }
//}
//package com.example.weathery.ViewModel
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.weathery.CurrentWeatherEntity
//import com.example.weathery.DailyForecastEntity
//import com.example.weathery.HourlyForecastEntity
//import com.example.weathery.WeatherRepository
//import kotlinx.coroutines.launch
//
//class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {
//    val currentWeather: LiveData<CurrentWeatherEntity> = repository.currentWeather
//    val hourlyForecasts: LiveData<List<HourlyForecastEntity>> = repository.hourlyForecasts
//    val dailyForecasts: LiveData<List<DailyForecastEntity>> = repository.dailyForecasts
//
//    private val _error = MutableLiveData<String?>()
//    val error: LiveData<String?> = _error
//
//    fun refreshWeatherData(lat: Double, lon: Double) {
//        viewModelScope.launch {
//            try {
//                repository.refreshWeatherData(lat, lon)
//                _error.value = null
//            } catch (e: Exception) {
//                _error.value = "Failed to load weather data: ${e.message}"
//            }
//        }
//    }
//}



//package com.example.weatherapp
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.weathery.Model.ForecastItemEntity
//import com.example.weathery.Model.WeatherDao
//import com.example.weathery.WeatherDatabase
//import com.example.weathery.WeatherRepository
//import com.example.weathery.data.WeatherRemoteDataSource
//import kotlinx.coroutines.launch
//import java.text.SimpleDateFormat
//import java.util.*
//
//data class WeatherData(
//    val dateTime: Long,
//    val main: Main,
//    val weather: List<Weather>,
//    val clouds: Clouds,
//    val wind: Wind,
//    val visibility: Int,
//    val sys: Sys
//)
//
//data class Main(val temp: Double, val pressure: Int, val humidity: Int)
//data class Weather(val description: String, val icon: String)
//data class Clouds(val all: Int)
//data class Wind(val speed: Double)
//data class Sys(val pod: String)
//data class WeatherResponse(val city: City, val weatherList: List<WeatherData>)
//data class City(val name: String)
//
//data class DailyForecast(
//    val date: String,
//    val minTemp: Double,
//    val maxTemp: Double,
//    val weather: Weather?,
//    val pod: String
//)
//
//class WeatherViewModel : ViewModel() {
//    private val _currentWeather = MutableLiveData<WeatherData>()
//    val currentWeather: LiveData<WeatherData> = _currentWeather
//
//    private val _hourlyForecast = MutableLiveData<List<WeatherData>>()
//    val hourlyForecast: LiveData<List<WeatherData>> = _hourlyForecast
//
//    private val _dailyForecast = MutableLiveData<List<DailyForecast>>()
//    val dailyForecast: LiveData<List<DailyForecast>> = _dailyForecast
//
//    private val _weatherResponse = MutableLiveData<WeatherResponse>()
//    val weatherResponse: LiveData<WeatherResponse> = _weatherResponse
//
//    private val _forecasts = MutableLiveData<List<ForecastItemEntity>>()
//    val remote = WeatherRemoteDataSource()
//    val dao = WeatherDatabase.getDatabase()
//    val repo=WeatherRepository(dao , remote)
//
//    fun loadForecasts(lat: Double, lon: Double, apiKey: String) {
//        viewModelScope.launch {
//            // Simulate API call with sample data
//            repo.refreshForecast(lat, lon,apiKey)
//            val sampleData = WeatherResponse(
//                city = City("Giza Governorate"),
//                weatherList = listOf(
//                    WeatherData(
//                        dateTime = System.currentTimeMillis() / 1000,
//                        main = Main(13.0, 1021, 77),
//                        weather = listOf(Weather("Clear sky", "01n")),
//                        clouds = Clouds(0),
//                        wind = Wind(2.06),
//                        visibility = 6000,
//                        sys = Sys("n")
//                    ),
//                    // Add more hourly data...
//                )
//            )
//            _weatherResponse.value = sampleData
//            processWeatherData(sampleData)
//        }
//    }
//
//    private fun processWeatherData(response: WeatherResponse) {
//        val weatherList = response.weatherList
//        if (weatherList.isNotEmpty()) {
//            _currentWeather.value = weatherList.first()
//            _hourlyForecast.value = weatherList.take(5)
//
//            val dailyMap = weatherList.groupBy { dateTimeToDate(it.dateTime) }
//            _dailyForecast.value = dailyMap.map { (date, list) ->
//                val minTemp = list.minOf { it.main.temp }
//                val maxTemp = list.maxOf { it.main.temp }
//                val representativeWeather = list.first()
//                DailyForecast(date, minTemp, maxTemp, representativeWeather.weather.firstOrNull(), representativeWeather.sys.pod)
//            }
//        }
//    }
//
//    private fun dateTimeToDate(timestamp: Long): String {
//        val date = Date(timestamp * 1000)
//        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
//    }
//}