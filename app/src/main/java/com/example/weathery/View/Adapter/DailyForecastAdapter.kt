package com.example.weathery.View.Adapter

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
import com.example.weathery.Model.ForecastItemEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DailyForecastAdapter
    : ListAdapter<ForecastItemEntity, DailyForecastAdapter.DailyForecastViewHolder>(DailyForecastDiffCallback()) {

    class DailyForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dayText: TextView = itemView.findViewById(R.id.dayText)
        private val tempRangeText: TextView = itemView.findViewById(R.id.tempRangeText)
        private val weatherIcon: ImageView = itemView.findViewById(R.id.weatherIcon)
        private val descriptionText: TextView = itemView.findViewById(R.id.descriptionText)

        fun bind(forecast: ForecastItemEntity) {
            // Day of week
            val date = Date(forecast.dateTime * 1_000)
            val sdf = SimpleDateFormat("EEE", Locale.getDefault())
            dayText.text = sdf.format(date)

            // Min / Max temp
            tempRangeText.text = "${forecast.tempMin}°C / ${forecast.tempMax}°C"

            // Icon & description
            loadWeatherIcon(weatherIcon, forecast.icon)
            descriptionText.text = forecast.description
        }

        private fun loadWeatherIcon(imageView: ImageView, iconCode: String) {
            val url = "https://openweathermap.org/img/wn/${iconCode}@2x.png"
            Glide.with(imageView.context)
                .load(url)
                .placeholder(R.drawable.ic_launcher_foreground) // optional placeholder
                .error(R.drawable.ic_launcher_background)             // optional error drawable
                .into(imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyForecastViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_daily_forecast, parent, false)
        return DailyForecastViewHolder(view)
    }

    override fun onBindViewHolder(holder: DailyForecastViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class DailyForecastDiffCallback : DiffUtil.ItemCallback<ForecastItemEntity>() {
    override fun areItemsTheSame(oldItem: ForecastItemEntity, newItem: ForecastItemEntity) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ForecastItemEntity, newItem: ForecastItemEntity) =
        oldItem == newItem
}
