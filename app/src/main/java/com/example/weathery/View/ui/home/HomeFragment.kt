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
import com.example.weathery.ForecastAdapter
import com.example.weathery.HourlyForecastAdapter
import com.example.weathery.View.Adapter.DailyForecastAdapter
import com.example.weathery.ViewModel.WeatherViewModel
import com.example.weathery.WeatherDatabase
import com.example.weathery.WeatherRepository
import com.example.weathery.databinding.FragmentHomeBinding
import com.example.weathery.work.LocationHelper
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var locHelper: LocationHelper
    private val viewModel: WeatherViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val database = WeatherDatabase.getDatabase(requireContext())
                val dao = database.weatherDao()
                return WeatherViewModel(WeatherRepository.create(dao)) as T
            }
        }
    }

    private val forecastAdapter = ForecastAdapter()
    private val hourlyAdapter = HourlyForecastAdapter()
    private val dailyAdapter = DailyForecastAdapter()

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
        binding.forecastRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.forecastRecyclerView.adapter = forecastAdapter

        binding.hourlyForecastRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.hourlyForecastRecyclerView.adapter = hourlyAdapter

        binding.dailyForecastRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.dailyForecastRecyclerView.adapter = dailyAdapter
    }

    private fun setupObservers() {
        viewModel.weatherResponse.observe(viewLifecycleOwner) { response ->
            binding.progressBar.isVisible = false
            response?.let {
                binding.locationText.text = it.city.name
                binding.dateText.text = getCurrentFormattedDate()
                // Add current weather updates if needed
                binding.weatherDescription.text = it.weatherList.firstOrNull()?.weather?.firstOrNull()?.description ?: "N/A"
                binding.currentTemperature.text = "${it.weatherList.firstOrNull()?.main?.temp?.toInt() ?: 0}°C"
                // Note: weatherIcon update requires image loading (e.g., Glide)
            }
        }

        viewModel.forecasts.observe(viewLifecycleOwner) { forecasts ->
            binding.progressBar.isVisible = false
            binding.weatherInfo.isVisible = true
            forecastAdapter.submitList(forecasts)
            hourlyAdapter.submitList(forecasts.take(5))
            dailyAdapter.submitList(forecasts)
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            binding.progressBar.isVisible = false
            if (error != null) {
                binding.errorText.isVisible = true
                binding.weatherInfo.isVisible = false
                binding.errorText.text = error
            } else {
                binding.errorText.isVisible = false
            }
        }
    }

    private fun getCurrentFormattedDate(): String {
        val sdf = SimpleDateFormat("EEE, dd MMM", Locale.getDefault())
        return sdf.format(Date())
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun requestLocation() {
        val apiKey = "9a3fd0649e44a6fb8826038be3aa48fc"
        locHelper = LocationHelper(requireContext() as AppCompatActivity)
        locHelper.requestLocation { lat, lon ->
            viewModel.loadForecasts(lat, lon, apiKey)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}