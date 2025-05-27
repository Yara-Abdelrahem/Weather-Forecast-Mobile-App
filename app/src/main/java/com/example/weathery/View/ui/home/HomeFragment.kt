package com.example.weathery.View.ui.home

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weathery.ForecastAdapter
import com.example.weathery.View.Adapter.DailyForecastAdapter
import com.example.weathery.HourlyForecastAdapter
import com.example.weathery.View.INavFragmaent
import com.example.weathery.ViewModel.WeatherViewModel
import com.example.weathery.WeatherDatabase
import com.example.weathery.WeatherRepository
import com.example.weathery.databinding.FragmentHomeBinding
import com.example.weathery.work.LocationHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var locHelper: LocationHelper
    private val viewModel: WeatherViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val database = WeatherDatabase.getDatabase(requireContext())
                val dao = database.weatherDao()
                @Suppress("UNCHECKED_CAST")
                return WeatherViewModel(WeatherRepository.create(dao)) as T
            }
        }
    }

    private val hourlyAdapter = HourlyForecastAdapter()
    private val dailyAdapter = DailyForecastAdapter()
    private val forecastAdapter = ForecastAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()
        setupObservers()
        requestLocation()
    }

    private fun setupRecyclerViews() {
        binding.hourlyForecastRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = hourlyAdapter
        }
        binding.dailyForecastRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = dailyAdapter
        }
        binding.forecastRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = forecastAdapter
        }
    }

    private fun setupObservers() {
        // Current weather + header
        viewModel.weatherResponse.observe(viewLifecycleOwner) { response ->
            binding.progressBar.isVisible = false
            response?.let {
                binding.weatherInfo.isVisible = true
                binding.locationText.text = it.city.name
                binding.dateText.text = getCurrentFormattedDate()
                // Description and temp from first forecast
                binding.weatherDescription.text = it.weatherList.firstOrNull()?.weather?.firstOrNull()?.description ?: "N/A"
                binding.currentTemperature.text = "${it.weatherList.firstOrNull()?.main?.temp?.toInt() ?: 0}°C"
                // Load icon
                it.weatherList.firstOrNull()?.weather?.firstOrNull()?.icon?.let { code ->
                    val url = "https://openweathermap.org/img/wn/${code}@2x.png"
                    Glide.with(this)
                        .load(url)
                        .into(binding.weatherIcon)
                }
            }
        }

        // Hourly today
        viewModel.hourlyForecasts.observe(viewLifecycleOwner) { hourly ->
            hourlyAdapter.submitList(hourly)
            // detailed info from first hour
            if (hourly.isNotEmpty()) {
                binding.detailedWeatherInfo.isVisible = true
                val first = hourly[0]
                binding.pressureText.text = "Pressure: ${first.pressure} hPa"
                binding.humidityText.text = "Humidity: ${first.humidity}%"
                binding.windText.text = "Wind: ${first.windSpeed} m/s"
                binding.cloudText.text = "Clouds: ${first.cloud}%"
                binding.uvIndexText.text = "UV Index: ${ "N/A"}"
                binding.visibilityText.text = "Visibility: ${first.visibility} m"
            }
        }

        // Daily summaries
        viewModel.dailySummaries.observe(viewLifecycleOwner) { daily ->
            dailyAdapter.submitList(daily)
        }

        // Full list (optional)
        viewModel.forecasts.observe(viewLifecycleOwner) { all ->
            forecastAdapter.submitList(all)
        }

        // Errors
        viewModel.error.observe(viewLifecycleOwner) { error ->
            binding.progressBar.isVisible = false
            binding.errorText.isVisible = error != null
            binding.errorText.text = error
        }
    }

    private fun getCurrentFormattedDate(): String =
        SimpleDateFormat("EEE, dd MMM", Locale.getDefault()).format(Date())

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun requestLocation() {
        val apiKey = "9a3fd0649e44a6fb8826038be3aa48fc"
        locHelper = LocationHelper(requireActivity() as AppCompatActivity)
        locHelper.requestLocation { lat, lon ->
            viewModel.loadForecasts(lat, lon, apiKey)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



//package com.example.weathery.View.ui.home
//
//import android.Manifest
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.annotation.RequiresPermission
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.isVisible
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.viewModels
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.weathery.ForecastAdapter
//import com.example.weathery.HourlyForecastAdapter
//import com.example.weathery.View.Adapter.DailyForecastAdapter
//import com.example.weathery.ViewModel.WeatherViewModel
//import com.example.weathery.WeatherDatabase
//import com.example.weathery.WeatherRepository
//import com.example.weathery.databinding.FragmentHomeBinding
//import com.example.weathery.work.LocationHelper
//import java.text.SimpleDateFormat
//import java.util.*
//
//class HomeFragment : Fragment() {
//
//    private var _binding: FragmentHomeBinding? = null
//    private val binding get() = _binding!!
//
//    private lateinit var locHelper: LocationHelper
//    private val viewModel: WeatherViewModel by viewModels {
//        object : ViewModelProvider.Factory {
//            override fun <T : ViewModel> create(modelClass: Class<T>): T {
//                val database = WeatherDatabase.getDatabase(requireContext())
//                val dao = database.weatherDao()
//                return WeatherViewModel(WeatherRepository.create(dao)) as T
//            }
//        }
//    }
//
//    private val forecastAdapter = ForecastAdapter()
//    private val hourlyAdapter = HourlyForecastAdapter()
//    private val dailyAdapter = DailyForecastAdapter()
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentHomeBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        setupRecyclerViews()
//        setupObservers()
//        requestLocation()
//    }
//
//    private fun setupRecyclerViews() {
////        binding.forecastRecyclerView.layoutManager = LinearLayoutManager(requireContext())
////        binding.forecastRecyclerView.adapter = forecastAdapter
//
//        binding.hourlyForecastRecyclerView.layoutManager =
//            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        binding.hourlyForecastRecyclerView.adapter = hourlyAdapter
//
//        binding.dailyForecastRecyclerView.layoutManager =
//            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        binding.dailyForecastRecyclerView.adapter = dailyAdapter
//    }
//
//    private fun setupObservers() {
//        viewModel.weatherResponse.observe(viewLifecycleOwner) { response ->
//            binding.progressBar.isVisible = false
//            response?.let {
//                binding.locationText.text = it.city.name
//                binding.dateText.text = getCurrentFormattedDate()
//                // Add current weather updates if needed
//                binding.weatherDescription.text = it.weatherList.firstOrNull()?.weather?.firstOrNull()?.description ?: "N/A"
//                binding.currentTemperature.text = "${it.weatherList.firstOrNull()?.main?.temp?.toInt() ?: 0}°C"
//                // Note: weatherIcon update requires image loading (e.g., Glide)
//            }
//        }
//
//        viewModel.forecasts.observe(viewLifecycleOwner) { forecasts ->
//            binding.progressBar.isVisible = false
////            binding.detailedWeatherInfo.isVisible = true
//            forecastAdapter.submitList(forecasts)
//            hourlyAdapter.submitList(forecasts)
//            dailyAdapter.submitList(forecasts)
//        }
//
//        viewModel.error.observe(viewLifecycleOwner) { error ->
//            binding.progressBar.isVisible = false
//            if (error != null) {
//                binding.errorText.isVisible = true
////                binding.detailedWeatherInfo.isVisible = false
//                binding.errorText.text = error
//            } else {
//                binding.errorText.isVisible = false
//            }
//        }
//    }
//
//    private fun getCurrentFormattedDate(): String {
//        val sdf = SimpleDateFormat("EEE, dd MMM", Locale.getDefault())
//        return sdf.format(Date())
//    }
//
//    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
//    private fun requestLocation() {
//        val apiKey = "9a3fd0649e44a6fb8826038be3aa48fc"
//        locHelper = LocationHelper(requireContext() as AppCompatActivity)
//        locHelper.requestLocation { lat, lon ->
//            viewModel.loadForecasts(lat, lon, apiKey)
//        }
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}

//package com.example.weathery.View.ui.home
//
//import android.Manifest
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.annotation.RequiresPermission
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.isVisible
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.viewModels
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.weathery.HourlyForecastAdapter
//import com.example.weathery.ViewModel.WeatherViewModel
//import com.example.weathery.WeatherDatabase
//import com.example.weathery.WeatherRepository
//import com.example.weathery.databinding.FragmentHomeBinding
//import com.example.weathery.work.LocationHelper
//import java.text.SimpleDateFormat
//import java.util.*
//
//class HomeFragment : Fragment() {
//
//    private var _binding: FragmentHomeBinding? = null
//    private val binding get() = _binding!!
//
//    private lateinit var locHelper: LocationHelper
//    private val viewModel: WeatherViewModel by viewModels {
//        object : ViewModelProvider.Factory {
//            override fun <T : ViewModel> create(modelClass: Class<T>): T {
//                val database = WeatherDatabase.getDatabase(requireContext())
//                val repository = WeatherRepository(
//                    database.currentWeatherDao(),
//                    database.hourlyForecastDao(),
//                    database.dailyForecastDao()
//                )
//                return WeatherViewModel(repository) as T
//            }
//        }
//    }
//
//    private val hourlyAdapter = HourlyForecastAdapter()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
////        val permissionLauncher = arguments?.getSerializable("permissionLauncher") as? LocationHelper.PermissionLauncher
//        locHelper = LocationHelper(requireActivity() as AppCompatActivity)
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentHomeBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        setupRecyclerViews()
//        setupObservers()
//        setupToolbar()
//        requestLocation()
//    }
//
//    private fun setupRecyclerViews() {
//        binding.hourlyForecastRecyclerView.layoutManager =
//            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        binding.hourlyForecastRecyclerView.adapter = hourlyAdapter
//    }
//
//    private fun setupObservers() {
//        viewModel.currentWeather.observe(viewLifecycleOwner) { current ->
//            binding.progressBar.isVisible = false
//            binding.weatherInfo.isVisible = true
//            binding.locationText.text = current.cityName
//            binding.dateText.text = getCurrentFormattedDate()
//            binding.weatherDescription.text = current.description
//            binding.currentTemperature.text = String.format("%.1f°C", current.temperature)
//            // Load weather icon (e.g., using Glide)
//            // Glide.with(binding.weatherIcon.context).load("https://openweathermap.org/img/wn/${current.icon}@2x.png").into(binding.weatherIcon)
//        }
//
//        viewModel.hourlyForecasts.observe(viewLifecycleOwner) { hourly ->
//            binding.progressBar.isVisible = false
//            binding.weatherInfo.isVisible = true
//            hourlyAdapter.submitList(hourly)
//        }
//
//        viewModel.dailyForecasts.observe(viewLifecycleOwner) { daily ->
//            binding.progressBar.isVisible = false
//            binding.weatherInfo.isVisible = true
//            if (daily.isNotEmpty()) {
//                val tomorrow = daily[0]
//                binding.tomorrowLabel.text = "Tomorrow"
//                binding.tomorrowTemp.text = String.format("%.1f / %.1f°C", tomorrow.tempMax, tomorrow.tempMin)
//                // Load icon (e.g., using Glide)
//                // Glide.with(binding.tomorrowIcon.context).load("https://openweathermap.org/img/wn/${tomorrow.icon}@2x.png").into(binding.tomorrowIcon)
//            }
//            if (daily.size >= 2) {
//                val nextDay = daily[1]
//                binding.nextDayLabel.text = getDayName(nextDay.dateTime)
//                binding.nextDayTemp.text = String.format("%.1f / %.1f°C", nextDay.tempMax, nextDay.tempMin)
//                // Load icon (e.g., using Glide)
//                // Glide.with(binding.nextDayIcon.context).load("https://openweathermap.org/img/wn/${nextDay.icon}@2x.png").into(binding.nextDayIcon)
//            }
//        }
//
//        viewModel.error.observe(viewLifecycleOwner) { error ->
//            binding.progressBar.isVisible = false
//            if (error != null) {
//                binding.errorText.isVisible = true
//                binding.weatherInfo.isVisible = false
//                binding.errorText.text = error
//            } else {
//                binding.errorText.isVisible = false
//            }
//        }
//    }
//
//    private fun setupToolbar() {
//        binding.toolbar.setNavigationOnClickListener {
//            // Handle navigation icon click (e.g., open navigation drawer)
//        }
//    }
//
//    private fun getCurrentFormattedDate(): String {
//        val sdf = SimpleDateFormat("EEE, dd MMM", Locale.getDefault())
//        return sdf.format(Date())
//    }
//
//    private fun getDayName(timestamp: Long): String {
//        val calendar = Calendar.getInstance().apply { timeInMillis = timestamp * 1000 }
//        return SimpleDateFormat("EEE", Locale.getDefault()).format(calendar.time)
//    }
//
//    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
//    private fun requestLocation() {
//        val apiKey = "9a3fd0649e44a6fb8826038be3aa48fc"
//        locHelper.requestLocation { lat, lon ->
//            viewModel.refreshWeatherData(lat, lon)
//        }
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//
////    companion object {
////        fun newInstance(permissionLauncher: LocationHelper.PermissionLauncher): HomeFragment {
////            val fragment = HomeFragment()
////            fragment.arguments = Bundle().apply {
////                putSerializable("permissionLauncher", permissionLauncher)
////            }
////            return fragment
////        }
////    }
//}
