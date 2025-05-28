package com.example.weathery.View.ui.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weathery.ForecastAdapter
import com.example.weathery.Home.View.DailyForecastAdapter
import com.example.weathery.HourlyForecastAdapter
import com.example.weathery.Home.ViewModel.WeatherViewModel
import com.example.weathery.Home.LocationHelper
import com.example.weathery.WeatherDatabase
import com.example.weathery.WeatherRepository
import com.example.weathery.databinding.FragmentHomeBinding
import com.example.weathery.View.HomeActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeFragment() : Fragment() {

    // optional secondary constructor for “favorite city” usage
    private var customLat: Double? = null
    private var customLon: Double? = null
    private var favcity: Boolean = false
    constructor(lat: Double, lon: Double, favcity: Boolean) : this() {
        this.customLat = lat
        this.customLon = lon
        this.favcity = favcity
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var locHelper: LocationHelper
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    private val hourlyAdapter = HourlyForecastAdapter()
    private val dailyAdapter = DailyForecastAdapter()
    private val forecastAdapter = ForecastAdapter()

    private lateinit var temperatureUnit: String
    private lateinit var windSpeedUnit: String
    private lateinit var locationMethod: String

    private val viewModel: WeatherViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val dao = WeatherDatabase.getDatabase(requireContext()).weatherDao()
                @Suppress("UNCHECKED_CAST")
                return WeatherViewModel(WeatherRepository.create(dao)) as T
            }
        }
    }

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

        // 1) toolbar + lists
        binding.toolbar.apply {
            title = "Home"
            setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_menu_overflow_material)
        }
        binding.hourlyForecastRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.hourlyForecastRecyclerView.adapter = hourlyAdapter

        binding.dailyForecastRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())
        binding.dailyForecastRecyclerView.adapter = dailyAdapter
        binding.dailyForecastRecyclerView.isNestedScrollingEnabled = true

        // 2) load prefs
        val prefs = requireContext()
            .getSharedPreferences("weather_prefs", Context.MODE_PRIVATE)
        temperatureUnit = prefs.getString("temperature_unit", "Kelvin")!!
        windSpeedUnit    = prefs.getString("wind_speed_unit", "meter/sec")!!
        locationMethod   = prefs.getString("location_method", "GPS")!!

        hourlyAdapter.temperatureUnit = temperatureUnit
        dailyAdapter.temperatureUnit  = temperatureUnit

        // 3) observers
        setupObservers()

        // 4) permission launcher
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { perms ->
            val fine = perms[Manifest.permission.ACCESS_FINE_LOCATION] == true
            if (fine && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) else {
                binding.progressBar.isVisible = false
                binding.errorText.apply {
                    text = "Location permission denied"
                    isVisible = true
                }
            }
        }

        // 5) init helper
        locHelper = LocationHelper(requireActivity() as HomeActivity, permissionLauncher)

        // 6) decide which location to use
        val apiKey = "9a3fd0649e44a6fb8826038be3aa48fc"
        when {
            // 6a) GPS mode
            locationMethod == "GPS" && customLat == null -> {
                locHelper.getCurrentLocation { lat, lon ->
                    viewModel.loadForecasts(lat, lon, apiKey)
                }
            }
            // 6b) Map mode (non‐favorite)
            locationMethod == "Map" && !favcity -> {
                val latF = prefs.getFloat("saved_latitude", 0f)
                val lonF = prefs.getFloat("saved_longitude", 0f)
                if (latF != 0f && lonF != 0f) {
                    viewModel.loadForecasts(latF.toDouble(), lonF.toDouble(), apiKey)
                } else {
                    binding.progressBar.isVisible = false
                    binding.errorText.apply {
                        text = "Please select a map location"
                        isVisible = true
                    }
                }
            }
            // 6c) Favorite‐city override
            customLat != null && customLon != null && favcity -> {
                viewModel.loadForecasts(customLat!!, customLon!!, apiKey)
            }
            else -> {
                binding.progressBar.isVisible = false
                binding.errorText.apply {
                    text = "Unable to determine location"
                    isVisible = true
                }
            }
        }
    }

    private fun setupObservers() {
        binding.progressBar.isVisible = true
        viewModel.weatherResponse.observe(viewLifecycleOwner) { resp ->
            binding.progressBar.isVisible = false
            resp?.let { w ->
                binding.locationText.text = w.city.name
                binding.dateText.text = getCurrentFormattedDate()
                val first = w.weatherList.firstOrNull()
                binding.weatherDescription.text =
                    first?.weather?.firstOrNull()?.description ?: "N/A"

                // temp
                val t = first?.main?.temp ?: 0.0
                val fmt = String.format("%.0f%s",
                    convertTemperature(t, temperatureUnit),
                    when (temperatureUnit) {
                        "Celsius"    -> "°C"
                        "Fahrenheit" -> "°F"
                        else         -> "K"
                    }
                )
                binding.currentTemperature.text = fmt

                first?.weather?.firstOrNull()?.icon?.let { code ->
                    Glide.with(this).load(
                        "https://openweathermap.org/img/wn/${code}@2x.png"
                    ).into(binding.weatherIcon)
                }
            }
        }

        viewModel.hourlyForecasts.observe(viewLifecycleOwner) { list ->
            hourlyAdapter.submitList(list)
            if (list.isNotEmpty()) {
                binding.detailedWeatherInfo.isVisible = true
                val f = list[0]
                binding.pressureText.text = "${f.pressure} hPa"
                binding.humidityText.text = "${f.humidity}%"
                val ws = convertWindSpeed(f.windSpeed, windSpeedUnit)
                binding.windText.text = String.format("%.1f %s", ws,
                    if (windSpeedUnit == "meter/sec") "m/s" else "mph"
                )
            }
        }

        viewModel.dailySummaries.observe(viewLifecycleOwner) {
            dailyAdapter.submitList(it)
        }
        viewModel.forecasts.observe(viewLifecycleOwner) {
            forecastAdapter.submitList(it)
        }
        viewModel.error.observe(viewLifecycleOwner) { err ->
            binding.progressBar.isVisible = false
            binding.errorText.apply {
                text = err
                isVisible = err != null
            }
        }
    }

    private fun convertTemperature(k: Double, unit: String): Double =
        when (unit) {
            "Celsius"    -> k - 273.15
            "Fahrenheit" -> (k - 273.15) * 9/5 + 32
            else         -> k
        }

    private fun convertWindSpeed(mps: Double, unit: String): Double =
        if (unit == "miles/hour") mps * 2.23694 else mps

    private fun getCurrentFormattedDate(): String =
        SimpleDateFormat("EEE, dd MMM", Locale.getDefault()).format(Date())

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val apiKey = "9a3fd0649e44a6fb8826038be3aa48fc"
    }
}
