//package com.example.weathery
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.ListAdapter
//import androidx.recyclerview.widget.RecyclerView
//import com.example.weathery.Model.ForecastItemEntity
//import java.text.SimpleDateFormat
//import java.util.Date
//import java.util.Locale
//
//class ForecastAdapter : ListAdapter<ForecastItemEntity, ForecastAdapter.ForecastViewHolder>(ForecastDiffCallback()) {
//
//    class ForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val dateTimeText: TextView = itemView.findViewById(R.id.dateTimeText)
//        private val tempText: TextView = itemView.findViewById(R.id.tempText)
//        private val feelsLikeText: TextView = itemView.findViewById(R.id.feelsLikeText)
//        private val tempMinMaxText: TextView = itemView.findViewById(R.id.tempMinMaxText)
//        private val pressureText: TextView = itemView.findViewById(R.id.pressureText)
//        private val humidityText: TextView = itemView.findViewById(R.id.humidityText)
//        private val windText: TextView = itemView.findViewById(R.id.windText)
//        private val cloudText: TextView = itemView.findViewById(R.id.cloudText)
//        private val visibilityText: TextView = itemView.findViewById(R.id.visibilityText)
//        private val descriptionText: TextView = itemView.findViewById(R.id.descriptionText)
//
//        fun bind(forecast: ForecastItemEntity) {
//            val date = Date(forecast.dateTime * 1000)
//            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
//            dateTimeText.text = sdf.format(date)
//            tempText.text = "${forecast.temp}°K"
//            feelsLikeText.text = "Feels like: ${forecast.feelsLike}°C"
//            tempMinMaxText.text = "Min: ${forecast.tempMin}°K / Max: ${forecast.tempMax}°K"
//            pressureText.text = "Pressure: ${forecast.pressure} hPa"
//            humidityText.text = "Humidity: ${forecast.humidity}%"
//            windText.text = "Wind: ${forecast.windSpeed} m/s"
//            cloudText.text = "Cloudiness: ${forecast.cloud}%"
//            visibilityText.text = "Visibility: ${forecast.visibility} m"
//            descriptionText.text = forecast.description
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_forecast, parent, false)
//        return ForecastViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
//        holder.bind(getItem(position))
//    }
//    class ForecastDiffCallback : DiffUtil.ItemCallback<ForecastItemEntity>() {
//        override fun areItemsTheSame(oldItem: ForecastItemEntity, newItem: ForecastItemEntity): Boolean {
//            return oldItem.id == newItem.id
//        }
//
//        override fun areContentsTheSame(oldItem: ForecastItemEntity, newItem: ForecastItemEntity): Boolean {
//            return oldItem == newItem
//        }
//    }
//}
//
