package com.example.weathery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weathery.Home.Model.ForecastItemEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HourlyForecastAdapter : ListAdapter<ForecastItemEntity, HourlyForecastAdapter.HourlyForecastViewHolder>(HourlyForecastDiffCallback()) {

    var temperatureUnit: String = "Kelvin"

    class HourlyForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val timeText: TextView = itemView.findViewById(R.id.timeText)
        private val tempText: TextView = itemView.findViewById(R.id.tempText)
        private val weatherIcon: ImageView = itemView.findViewById(R.id.weatherIcon)
        private val descriptionText: TextView = itemView.findViewById(R.id.descriptionText)

        fun bind(forecast: ForecastItemEntity, temperatureUnit: String) {
            val date = Date(forecast.dateTime * 1000)
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            timeText.text = sdf.format(date)

            // Assume HomeFragment provides a way to call convertTemperature
            val tempKelvin = forecast.temp
            val displayTemp = when (temperatureUnit) {
                "Celsius" -> tempKelvin - 273.15
                "Fahrenheit" -> (tempKelvin - 273.15) * 9 / 5 + 32
                "Kelvin" -> tempKelvin
                else -> tempKelvin
            }
            val tempUnitSymbol = when (temperatureUnit) {
                "Celsius" -> "°C"
                "Fahrenheit" -> "°F"
                "Kelvin" -> "K"
                else -> "K"
            }
            tempText.text = String.format("%.0f%s", displayTemp, tempUnitSymbol)

            loadWeatherIcon(weatherIcon, forecast.icon)
            descriptionText.text = forecast.description
        }

        private fun loadWeatherIcon(imageView: ImageView, iconCode: String) {
            val url = "https://openweathermap.org/img/wn/${iconCode}@2x.png"
            Glide.with(imageView.context).load(url).into(imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyForecastViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_hourly_forecast, parent, false)
        return HourlyForecastViewHolder(view)
    }

    override fun onBindViewHolder(holder: HourlyForecastViewHolder, position: Int) {
        holder.bind(getItem(position), temperatureUnit)
    }
}

class HourlyForecastDiffCallback : DiffUtil.ItemCallback<ForecastItemEntity>() {
    override fun areItemsTheSame(oldItem: ForecastItemEntity, newItem: ForecastItemEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ForecastItemEntity, newItem: ForecastItemEntity): Boolean {
        return oldItem == newItem
    }
}