//package com.example.weathery.View.Adapter
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.ListAdapter
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.example.weathery.DailyForecastEntity
//import com.example.weathery.databinding.ItemDailyForecastBinding
//import java.text.SimpleDateFormat
//import java.util.*
//
//class DailyForecastAdapter : ListAdapter<DailyForecastEntity, DailyForecastAdapter.DailyViewHolder>(DailyDiffCallback()) {
//
//    class DailyViewHolder(private val binding: ItemDailyForecastBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(forecast: DailyForecastEntity, position: Int) {
//            // Set day label
//            binding.dayText.text = if (position == 0) "Tomorrow" else getDayName(forecast.dateTime)
//
//            // Set temperature range
//            binding.tempRangeText.text = String.format("%.1f / %.1f°C", forecast.tempMax, forecast.tempMin)
//
//            // Set weather description
//            binding.descriptionText.text = forecast.description
//
//            // Load weather icon using Glide
//            Glide.with(binding.weatherIcon.context)
//                .load("https://openweathermap.org/img/wn/${forecast.icon}@2x.png")
//                .into(binding.weatherIcon)
//        }
//
//        private fun getDayName(timestamp: Long): String {
//            val calendar = Calendar.getInstance().apply { timeInMillis = timestamp * 1000 }
//            return SimpleDateFormat("EEE", Locale.getDefault()).format(calendar.time)
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
//        val binding = ItemDailyForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return DailyViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
//        holder.bind(getItem(position), position)
//    }
//}
//
//class DailyDiffCallback : DiffUtil.ItemCallback<DailyForecastEntity>() {
//    override fun areItemsTheSame(oldItem: DailyForecastEntity, newItem: DailyForecastEntity): Boolean {
//        return oldItem.id == newItem.id
//    }
//
//    override fun areContentsTheSame(oldItem: DailyForecastEntity, newItem: DailyForecastEntity): Boolean {
//        return oldItem == newItem
//    }
//}


package com.example.weathery.View.Adapter

import com.bumptech.glide.Glide
import com.example.weathery.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weathery.Model.ForecastItemEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DailyForecastAdapter : ListAdapter<ForecastItemEntity, DailyForecastAdapter.DailyForecastViewHolder>(DailyForecastDiffCallback()) {

    class DailyForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dayText: TextView = itemView.findViewById(R.id.dayText)
        private val tempRangeText: TextView = itemView.findViewById(R.id.tempRangeText)
        private val weatherIcon: ImageView = itemView.findViewById(R.id.weatherIcon)
        private val descriptionText: TextView = itemView.findViewById(R.id.descriptionText)
//        private val cardView: CardView = itemView.findViewById(R.id.currentWeatherCard)  // Assuming this ID exists

        fun bind(forecast: ForecastItemEntity, position: Int) {
            val date = Date(forecast.dateTime * 1_000)
            val sdf = SimpleDateFormat("EEE", Locale.getDefault())

            if (position == 0) {
                dayText.text = "Tomorrow"
//                cardView.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.gradient_start))
            } else {
                dayText.text = sdf.format(date)
//                cardView.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.dark_gray))
            }

            tempRangeText.text = "${forecast.tempMin}°C / ${forecast.tempMax}°C"
            loadWeatherIcon(weatherIcon, forecast.icon)
            descriptionText.text = forecast.description
        }

        private fun loadWeatherIcon(imageView: ImageView, iconCode: String) {
            val url = "[invalid url, do not cite]"
            Glide.with(imageView.context).load(url).into(imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyForecastViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_daily_forecast, parent, false)
        return DailyForecastViewHolder(view)
    }

    override fun onBindViewHolder(holder: DailyForecastViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }
}

class DailyForecastDiffCallback : DiffUtil.ItemCallback<ForecastItemEntity>() {
    override fun areItemsTheSame(oldItem: ForecastItemEntity, newItem: ForecastItemEntity) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ForecastItemEntity, newItem: ForecastItemEntity) =
        oldItem == newItem
}
