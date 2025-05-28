package com.example.weathery.View.ui.home

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresPermission
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weathery.ForecastAdapter
import com.example.weathery.Home.Adapter.DailyForecastAdapter
import com.example.weathery.HourlyForecastAdapter
import com.example.weathery.Home.ViewModel.WeatherViewModel
import com.example.weathery.WeatherDatabase
import com.example.weathery.WeatherRepository
import com.example.weathery.databinding.FragmentHomeBinding
import com.example.weathery.Home.LocationHelper
import com.example.weathery.View.HomeActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale



class HomeFragment() : Fragment() {
    //
    // secondary constructor for custom lat/lon
    private var customLat: Double? = null
    private var customLon: Double? = null

    constructor(lat: Double, lon: Double) : this() {
        this.customLat = lat
        this.customLon = lon
    }


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

    private lateinit var temperatureUnit: String
    private lateinit var windSpeedUnit: String
    private lateinit var locationMethod: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresPermission(allOf = [
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ])
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupRecyclerViews()

        val prefs = requireContext().getSharedPreferences("weather_prefs", Context.MODE_PRIVATE)
        temperatureUnit = prefs.getString("temperature_unit", "Kelvin") ?: "Kelvin"
        windSpeedUnit = prefs.getString("wind_speed_unit", "meter/sec") ?: "meter/sec"
        locationMethod = prefs.getString("location_method", "GPS") ?: "GPS"

        hourlyAdapter.temperatureUnit = temperatureUnit
        dailyAdapter.temperatureUnit = temperatureUnit

        setupObservers()

        val apiKey = "9a3fd0649e44a6fb8826038be3aa48fc"
        if (locationMethod == "GPS" &&customLon== null && customLat == null) {
            val activity = requireActivity() as HomeActivity
            locHelper = LocationHelper(activity, activity.permissionLauncher)
            locHelper.requestLocation { lat, lon ->
                viewModel.loadForecasts(lat, lon, apiKey)
            }
        } else if (locationMethod == "Map"&&customLon== null && customLat == null) {
            val savedLat = prefs.getFloat("saved_latitude", 0f)
            val savedLon = prefs.getFloat("saved_longitude", 0f)
            if (savedLat != 0f && savedLon != 0f) {
                viewModel.loadForecasts(savedLat.toDouble(), savedLon.toDouble(), apiKey)
            } else {
                binding.progressBar.isVisible = false
                binding.errorText.text = "Please select a location from the map."
                binding.errorText.isVisible = true
            }
        }else if (customLat!= null && customLon != null) {
            // hander case when location sent by favorite city
            val lat = customLat
            val lon = customLon
            if (lat != null && lon != null) {
                viewModel.loadForecasts(lat, lon, apiKey)
            } else {
                binding.progressBar.isVisible = false
                binding.errorText.apply {
                    text = "No coordinates provided for this city."
                    isVisible = true
                }
            }
        }
    }

    private fun setupToolbar() {
        binding.toolbar.title = "Home"
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_menu_overflow_material)
        binding.toolbar.setNavigationOnClickListener {
            // Handle toolbar menu if needed
        }
    }

    private fun setupRecyclerViews() {
        binding.hourlyForecastRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = hourlyAdapter
        }
        binding.dailyForecastRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = dailyAdapter
            isNestedScrollingEnabled = true
        }
    }

    private fun setupObservers() {
        viewModel.weatherResponse.observe(viewLifecycleOwner) { response ->
            binding.progressBar.isVisible = false
            response?.let {
                binding.locationText.text = it.city.name
                binding.dateText.text = getCurrentFormattedDate()

                val first = it.weatherList.firstOrNull()
                binding.weatherDescription.text = first?.weather?.firstOrNull()?.description ?: "N/A"

                val temp = first?.main?.temp ?: 0.0
                val displayTemp = convertTemperature(temp, temperatureUnit)
                val tempUnitSymbol = when (temperatureUnit) {
                    "Celsius" -> "°C"
                    "Fahrenheit" -> "°F"
                    "Kelvin" -> "K"
                    else -> "K"
                }
                binding.currentTemperature.text = String.format("%.0f%s", displayTemp, tempUnitSymbol)

                first?.weather?.firstOrNull()?.icon?.let { code ->
                    val url = "https://openweathermap.org/img/wn/${code}@2x.png"
                    Glide.with(this).load(url).into(binding.weatherIcon)
                }
            }
        }

        viewModel.hourlyForecasts.observe(viewLifecycleOwner) { hourly ->
            hourlyAdapter.submitList(hourly)
            if (hourly.isNotEmpty()) {
                binding.detailedWeatherInfo.isVisible = true
                val f = hourly[0]
                binding.pressureText.text = "${f.pressure} hPa"
                binding.humidityText.text = "${f.humidity}%"

                val windSpeed = f.windSpeed
                val displayWindSpeed = convertWindSpeed(windSpeed, windSpeedUnit)
                val windUnitSymbol = if (windSpeedUnit == "meter/sec") "m/s" else "mph"
                binding.windText.text = String.format("%.1f %s", displayWindSpeed, windUnitSymbol)
            }
        }

        viewModel.dailySummaries.observe(viewLifecycleOwner) { daily ->
            dailyAdapter.submitList(daily)
        }

        viewModel.forecasts.observe(viewLifecycleOwner) { all ->
            forecastAdapter.submitList(all)
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            binding.progressBar.isVisible = false
            binding.errorText.isVisible = error != null
            binding.errorText.text = error
        }
    }

    private fun convertTemperature(tempKelvin: Double, targetUnit: String): Double {
        return when (targetUnit) {
            "Celsius" -> tempKelvin - 273.15
            "Fahrenheit" -> (tempKelvin - 273.15) * 9 / 5 + 32
            else -> tempKelvin
        }
    }

    private fun convertWindSpeed(windSpeedMps: Double, targetUnit: String): Double {
        return when (targetUnit) {
            "miles/hour" -> windSpeedMps * 2.23694
            else -> windSpeedMps
        }
    }

    private fun getCurrentFormattedDate(): String =
        SimpleDateFormat("EEE, dd MMM", Locale.getDefault()).format(Date())

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
