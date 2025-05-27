//package com.example.weathery
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.ListAdapter
//import androidx.recyclerview.widget.RecyclerView
//import com.example.weathery.databinding.ItemHourlyForecastBinding
//import java.text.SimpleDateFormat
//import java.util.*
//
//class HourlyForecastAdapter : ListAdapter<HourlyForecastEntity, HourlyForecastAdapter.HourlyViewHolder>(HourlyDiffCallback()) {
//
//    class HourlyViewHolder(private val binding: ItemHourlyForecastBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(forecast: HourlyForecastEntity) {
//            val date = Date(forecast.dateTime * 1000)
//            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
//            binding.timeText.text = sdf.format(date)
//            binding.tempText.text = String.format("%.1f°C", forecast.temperature)
//            binding.descriptionText.text = forecast.description
//            // Load weather icon (e.g., using Glide)
//            // Glide.with(binding.weatherIcon.context).load("https://openweathermap.org/img/wn/${forecast.icon}@2x.png").into(binding.weatherIcon)
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
//        val binding = ItemHourlyForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return HourlyViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
//        holder.bind(getItem(position))
//    }
//}
//
//class HourlyDiffCallback : DiffUtil.ItemCallback<HourlyForecastEntity>() {
//    override fun areItemsTheSame(oldItem: HourlyForecastEntity, newItem: HourlyForecastEntity): Boolean {
//        return oldItem.id == newItem.id
//    }
//
//    override fun areContentsTheSame(oldItem: HourlyForecastEntity, newItem: HourlyForecastEntity): Boolean {
//        return oldItem == newItem
//    }
//}

//
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
import com.example.weathery.Model.ForecastItemEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// HourlyForecastAdapter
class HourlyForecastAdapter : ListAdapter<ForecastItemEntity, HourlyForecastAdapter.HourlyForecastViewHolder>(HourlyForecastDiffCallback()) {

    class HourlyForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val timeText: TextView = itemView.findViewById(R.id.timeText)
        private val tempText: TextView = itemView.findViewById(R.id.tempText)
        private val weatherIcon: ImageView = itemView.findViewById(R.id.weatherIcon)
        private val descriptionText: TextView = itemView.findViewById(R.id.descriptionText)

        fun bind(forecast: ForecastItemEntity) {
            val date = Date(forecast.dateTime * 1000)
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            timeText.text = sdf.format(date)
            tempText.text = "${forecast.temp}°C"
            // Assuming you have a method to load weather icons
            loadWeatherIcon(weatherIcon, forecast.icon)
            descriptionText.text = forecast.description
        }

        fun loadWeatherIcon(imageView: ImageView, iconCode: String) {
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
        holder.bind(getItem(position))
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
