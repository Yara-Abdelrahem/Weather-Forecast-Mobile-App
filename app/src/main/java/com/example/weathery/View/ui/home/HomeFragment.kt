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

    @RequiresPermission(allOf = [
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ])
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupRecyclerViews()
        setupObservers()
        requestLocation()
    }

    private fun setupToolbar() {
        binding.toolbar.title = "Home"
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_menu_overflow_material) // Use a menu icon
        binding.toolbar.setNavigationOnClickListener {
            // Handle navigation drawer opening here if needed
        }
    }

    private fun setupRecyclerViews() {
        binding.hourlyForecastRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = hourlyAdapter
            isNestedScrollingEnabled = false
        }
        binding.dailyForecastRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext()) // Vertical
            adapter = dailyAdapter
            isNestedScrollingEnabled = false
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
                binding.currentTemperature.text = "${first?.main?.temp?.toInt() ?: 0}°C"
                first?.weather?.firstOrNull()?.icon?.let { code ->
                    val url = "https://openweathermap.org/img/wn/${code}@2x.png"
                    Glide.with(this).load(url).into(binding.weatherIcon)
                    // Show stars for clear sky at night
//                    if (code == "01n") {
//                        binding.starLeft.isVisible = true
//                        binding.starRight.isVisible = true
//                    } else {
//                        binding.starLeft.isVisible = false
//                        binding.starRight.isVisible = false
//                    }
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
                binding.windText.text = "${f.windSpeed} m/s"
                //binding.cloudText.text = "${f.cloud}%"
                // UV index not shown unless data is available
                //binding.visibilityText.text = "${f.visibility} m"
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

    private fun getCurrentFormattedDate(): String =
        SimpleDateFormat("EEE, dd MMM", Locale.getDefault()).format(Date())

    @RequiresPermission(allOf = [
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ])
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