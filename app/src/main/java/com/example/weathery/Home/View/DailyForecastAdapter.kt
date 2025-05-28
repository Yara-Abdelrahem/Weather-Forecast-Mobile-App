package com.example.weathery.Home.View

import com.bumptech.glide.Glide
import com.example.weathery.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weathery.Home.Model.ForecastItemEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DailyForecastAdapter : ListAdapter<ForecastItemEntity, DailyForecastAdapter.DailyForecastViewHolder>(DailyForecastDiffCallback()) {

    var temperatureUnit: String = "Kelvin"

    class DailyForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dayText: TextView = itemView.findViewById(R.id.dayText)
        private val tempRangeText: TextView = itemView.findViewById(R.id.tempRangeText)
        private val weatherIcon: ImageView = itemView.findViewById(R.id.weatherIcon)
        private val descriptionText: TextView = itemView.findViewById(R.id.descriptionText)

        fun bind(forecast: ForecastItemEntity, position: Int, temperatureUnit: String) {
            val date = Date(forecast.dateTime * 1_000)
            val sdf = SimpleDateFormat("EEE", Locale.getDefault())

            if (position == 0) {
                dayText.text = "Tomorrow"
            } else {
                dayText.text = sdf.format(date)
            }

            // Convert temperatures from Kelvin
            val tempMinKelvin = forecast.tempMin
            val tempMaxKelvin = forecast.tempMax
            val displayMinTemp = when (temperatureUnit) {
                "Celsius" -> tempMinKelvin - 273.15
                "Fahrenheit" -> (tempMinKelvin - 273.15) * 9 / 5 + 32
                "Kelvin" -> tempMinKelvin
                else -> tempMinKelvin
            }
            val displayMaxTemp = when (temperatureUnit) {
                "Celsius" -> tempMaxKelvin - 273.15
                "Fahrenheit" -> (tempMaxKelvin - 273.15) * 9 / 5 + 32
                "Kelvin" -> tempMaxKelvin
                else -> tempMaxKelvin
            }
            val tempUnitSymbol = when (temperatureUnit) {
                "Celsius" -> "°C"
                "Fahrenheit" -> "°F"
                "Kelvin" -> "K"
                else -> "K"
            }
            tempRangeText.text = String.format("%.0f%s / %.0f%s", displayMinTemp, tempUnitSymbol, displayMaxTemp, tempUnitSymbol)

            loadWeatherIcon(weatherIcon, forecast.icon)
            descriptionText.text = forecast.description
        }

        private fun loadWeatherIcon(imageView: ImageView, iconCode: String) {
            val url = "https://openweathermap.org/img/wn/${iconCode}@2x.png"
            Glide.with(imageView.context).load(url).into(imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyForecastViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_daily_forecast, parent, false)
        return DailyForecastViewHolder(view)
    }

    override fun onBindViewHolder(holder: DailyForecastViewHolder, position: Int) {
        holder.bind(getItem(position), position, temperatureUnit)
    }
}

class DailyForecastDiffCallback : DiffUtil.ItemCallback<ForecastItemEntity>() {
    override fun areItemsTheSame(oldItem: ForecastItemEntity, newItem: ForecastItemEntity) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ForecastItemEntity, newItem: ForecastItemEntity) =
        oldItem == newItem
}