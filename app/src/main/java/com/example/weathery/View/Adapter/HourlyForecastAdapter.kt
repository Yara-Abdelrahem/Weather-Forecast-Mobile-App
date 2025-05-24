//package com.example.weathery.View
//
//
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.ListAdapter
//import androidx.recyclerview.widget.RecyclerView
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import com.bumptech.glide.Glide
//import com.example.weatherapp.WeatherData
//import com.example.weathery.databinding.ItemHourlyForecastBinding
//import java.text.SimpleDateFormat
//import java.util.Date
//import java.util.Locale
//
//class HourlyForecastAdapter : ListAdapter<WeatherData, HourlyForecastAdapter.ViewHolder>(WeatherDiffCallback()) {
//    class ViewHolder(val binding: ItemHourlyForecastBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(weather: WeatherData) {
//            val date = Date(weather.dateTime * 1000)
//            binding.timeText.text = SimpleDateFormat("h a", Locale.getDefault()).format(date)
//            Glide.with(binding.weatherIcon.context)
//                .load("http://openweathermap.org/img/wn/${weather.weather.firstOrNull()?.icon}.png")
//                .into(binding.weatherIcon)
//            binding.tempText.text = "${weather.main.temp}°C"
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val binding = ItemHourlyForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return ViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(getItem(position))
//    }
//}
//
//class WeatherDiffCallback : DiffUtil.ItemCallback<WeatherData>() {
//    override fun areItemsTheSame(oldItem: WeatherData, newItem: WeatherData) = oldItem.dateTime == newItem.dateTime
//    override fun areContentsTheSame(oldItem: WeatherData, newItem: WeatherData) = oldItem == newItem
//}