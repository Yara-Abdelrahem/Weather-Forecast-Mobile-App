package com.example.weathery

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weathery.Home.LocationHelper
import com.example.weathery.Home.ViewModel.WeatherViewModel
import com.example.weathery.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var locHelper: LocationHelper
    lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    private val viewModel: WeatherViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val database = WeatherDatabase.getDatabase(applicationContext)
                val dao = database.weatherDao()
                return WeatherViewModel(WeatherRepository.create(dao)) as T
            }
        }
    }
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupRecyclerView()
        setupObservers()
        // Replace with your API key
        val apiKey = "9a3fd0649e44a6fb8826038be3aa48fc"
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { perms ->
            // The callback is handled by LocationHelper, but we can log for debugging
            val granted = perms.all { it.value }
            Log.d("HomeActivity", "Permissions granted: $granted")
        }
        locHelper = LocationHelper(this, permissionLauncher)

        locHelper.requestLocation { lat, lon ->
            viewModel.loadForecasts(lat, lon, apiKey)
        }
    }

    private fun setupRecyclerView() {
        binding.forecastRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.forecastRecyclerView.adapter = ForecastAdapter()
    }

    private fun setupObservers() {
        viewModel.weatherResponse.observe(this) { response ->
            binding.progressBar.isVisible = false
            if (response != null) {
                binding.cityName.text = response.city.name
            }
        }

        viewModel.forecasts.observe(this) { forecasts ->
            binding.progressBar.isVisible = false
            binding.weatherInfo.isVisible = true
            (binding.forecastRecyclerView.adapter as ForecastAdapter).submitList(forecasts)
        }

        viewModel.error.observe(this) { error ->
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
}