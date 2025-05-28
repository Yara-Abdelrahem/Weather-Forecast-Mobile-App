package com.example.weathery

import com.example.weathery.Home.Model.ForecastItemEntity
import com.example.weathery.Model.WeatherDao
import com.example.weathery.Home.Model.WeatherResponse
import com.example.weathery.data.WeatherRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar

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
                            cityName = response.city.name,
                            latitude = response.city.coord.lat,
                            longitude = response.city.coord.lon
                        )
                    }
                    withContext(Dispatchers.IO) {
                        dao.clearAll()
                        clearOldForecastsForLocation(lat, lon)
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

    suspend fun clearOldForecastsForLocation(latitude: Double, longitude: Double) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val todayStart = calendar.timeInMillis
        dao.deleteOldForecasts(todayStart, latitude, longitude)
    }


    suspend fun getForecastsWithLatLong(lat: Double, lon: Double): List<ForecastItemEntity> {
        return withContext(Dispatchers.IO) {
            dao.getForecastsWithLatLong(lat, lon)
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
                    cityName    = cityName,
                    latitude    = items.first().latitude,
                    longitude   = items.first().longitude
                )
            }

        return hourlyToday to dailyAverages
    }
}