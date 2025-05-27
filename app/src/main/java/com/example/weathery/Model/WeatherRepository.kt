//package com.example.weathery
//
//import androidx.lifecycle.LiveData
//import com.example.weathery.Model.CurrentWeatherDao
//import com.example.weathery.Model.DailyForecastDao
//import com.example.weathery.Model.HourlyForecastDao
//
//class WeatherRepository(
//    private val currentWeatherDao: CurrentWeatherDao,
//    private val hourlyForecastDao: HourlyForecastDao,
//    private val dailyForecastDao: DailyForecastDao
//) {
//    val currentWeather: LiveData<CurrentWeatherEntity> = currentWeatherDao.getCurrentWeather()
//    val hourlyForecasts: LiveData<List<HourlyForecastEntity>> = hourlyForecastDao.getAllHourlyForecasts()
//    val dailyForecasts: LiveData<List<DailyForecastEntity>> = dailyForecastDao.getAllDailyForecasts()
//
//    suspend fun refreshWeatherData(lat: Double, lon: Double) {
//        try {
//            val response = WeatherRemoteDataSource.getWeatherData(lat, lon)
//            // Clear existing data
//            hourlyForecastDao.deleteAllHourlyForecasts()
//            dailyForecastDao.deleteAllDailyForecasts()
//            // Insert new data
//            val hourlyEntities = response.hourly.map {
//                HourlyForecastEntity(
//                    dateTime = it.dt,
//                    temperature = it.temp,
//                    description = it.weather[0].description,
//                    icon = it.weather[0].icon
//                )
//            }
//            hourlyForecastDao.insertHourlyForecasts(hourlyEntities)
//            val dailyEntities = response.daily.drop(1).map {
//                DailyForecastEntity(
//                    dateTime = it.dt,
//                    tempMin = it.temp.min,
//                    tempMax = it.temp.max,
//                    description = it.weather[0].description,
//                    icon = it.weather[0].icon
//                )
//            }
//            dailyForecastDao.insertDailyForecasts(dailyEntities)
//            // Insert current weather
//            val currentWeather = CurrentWeatherEntity(
//                cityName = response.city.name,
//                temperature = response.current.temp,
//                description = response.current.weather[0].description,
//                icon = response.current.weather[0].icon
//            )
//            currentWeatherDao.insertCurrentWeather(currentWeather)
//        } catch (e: Exception) {
//            // Handle error (e.g., log or expose to ViewModel)
//            throw e
//        }
//    }
//}

package com.example.weathery

import com.example.weathery.Model.ForecastItemEntity
import com.example.weathery.Model.WeatherDao
import com.example.weathery.Model.WeatherResponse
import com.example.weathery.data.WeatherRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
class WeatherRepository(
    private val dao: WeatherDao,
    private val weatherRemoteDataSource: WeatherRemoteDataSource
) {
    companion object {
        fun create(dao: WeatherDao): WeatherRepository {
            return WeatherRepository(dao, WeatherRemoteDataSource())
        }
    }

    suspend fun refreshForecast(lat: Double, lon: Double, apiKey: String): Result<WeatherResponse> {
        return weatherRemoteDataSource.getHourlyForecast(lat, lon, apiKey).also { result ->
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

    fun splitForecastItems(
        all: List<ForecastItemEntity>,
        zone: ZoneId = ZoneId.systemDefault()
    ): Pair<List<ForecastItemEntity>, List<ForecastItemEntity>> {
        // 1) compute LocalDate for each entry
        val entriesByDate: Map<LocalDate, List<ForecastItemEntity>> = all.groupBy { item ->
            Instant.ofEpochSecond(item.dateTime)
                .atZone(zone)
                .toLocalDate()
        }

        val today: LocalDate = LocalDate.now(zone)

        // 2) hourly list = everything for “today”
        val hourlyToday: List<ForecastItemEntity> =
            entriesByDate[today].orEmpty()

        // 3) for each date > today, compute one averaged ForecastItemEntity
        val dailyAverages: List<ForecastItemEntity> = entriesByDate
            .filterKeys { it.isAfter(today) }
            .toSortedMap()      // ensure ascending by date
            .map { (date, items) ->
                // compute averages
                val avgTemp      = items.map { it.temp }.average()
                val avgMin       = items.map { it.tempMin }.average()
                val avgMax       = items.map { it.tempMax }.average()
                val avgPressure  = items.map { it.pressure }.average()
                val avgHumidity  = items.map { it.humidity }.average()
                val avgWind      = items.map { it.windSpeed }.average()
                val avgCloud     = items.map { it.cloud }.average()
                val avgVisibility= items.map { it.visibility }.average()

                // pick the first description/icon as a representative
                val description  = items.first().description
                val icon         = items.first().icon

                // reuse the same cityName
                val cityName     = items.first().cityName

                // use the epoch at the start of that day for dateTime
                val epochAtMidnight = date
                    .atStartOfDay(zone)
                    .toEpochSecond()

                ForecastItemEntity(
                    // if your Entity has an 'id' PK, you can put 0 or any unused value here
                    dateTime    = epochAtMidnight,
                    temp        = avgTemp,
                    tempMin     = avgMin,
                    tempMax     = avgMax,
                    feelsLike   = items.map { it.feelsLike }.average(),
                    pressure    = avgPressure.toInt(),
                    humidity    = avgHumidity.toInt(),
                    windSpeed   = avgWind,
                    cloud       = avgCloud.toInt(),        // if 'cloud' is Int
                    visibility  = avgVisibility.toInt(),   // if 'visibility' is Int
                    description = description,
                    icon        = icon,
                    cityName    = cityName
                )
            }

        return hourlyToday to dailyAverages
    }
}