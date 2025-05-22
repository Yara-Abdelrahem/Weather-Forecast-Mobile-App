package com.example.weathery.Model

import androidx.room.TypeConverter
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

class WeatherTypeConverters {
    private val objectMapper = jacksonObjectMapper()

    @TypeConverter
    fun fromWeatherList(weatherList: ArrayList<Weather>?): String {
        return objectMapper.writeValueAsString(weatherList ?: emptyList<Weather>())
    }

    @TypeConverter
    fun toWeatherList(weatherString: String?): ArrayList<Weather>? {
        return weatherString?.let { objectMapper.readValue(it) } ?: arrayListOf()
    }
}