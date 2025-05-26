package com.example.weathery.View.ui.home

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weathery.ForecastAdapter
import com.example.weathery.View.INavFragmaent
import com.example.weathery.ViewModel.WeatherViewModel
import com.example.weathery.WeatherDatabase
import com.example.weathery.WeatherRepository
import com.example.weathery.databinding.FragmentHomeBinding
import com.example.weathery.work.LocationHelper
import java.util.jar.Manifest


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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
        requestLocation()
    }

    private fun setupRecyclerView() {
        binding.forecastRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.forecastRecyclerView.adapter = ForecastAdapter()
    }

    private fun setupObservers() {
        viewModel.weatherResponse.observe(viewLifecycleOwner) { response ->
            binding.progressBar.isVisible = false
            response?.let {
                binding.cityName.text = it.city.name
            }
        }

        viewModel.forecasts.observe(viewLifecycleOwner) { forecasts ->
            binding.progressBar.isVisible = false
            binding.weatherInfo.isVisible = true
            (binding.forecastRecyclerView.adapter as ForecastAdapter).submitList(forecasts)
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
//    // Register the activity result launcher as a class-level property
//    private val locationPermissionLauncher = registerForActivityResult(
//        ActivityResultContracts.RequestMultiplePermissions()
//    ) { permissions ->
//        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
//                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
//        if (granted) {
//            fetchLocation()
//        } else {
//            Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show()
//        }
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
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        setupRecyclerView()
//        setupObservers()
//        requestLocation()
//    }
//
//    private fun setupRecyclerView() {
//        binding.forecastRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//        binding.forecastRecyclerView.adapter = ForecastAdapter()
//    }
//
//    private fun setupObservers() {
//        viewModel.weatherResponse.observe(viewLifecycleOwner) { response ->
//            binding.progressBar.isVisible = false
//            response?.let {
//                binding.cityName.text = it.city.name
//            }
//        }
//
//        viewModel.forecasts.observe(viewLifecycleOwner) { forecasts ->
//            binding.progressBar.isVisible = false
//            binding.weatherInfo.isVisible = true
//            (binding.forecastRecyclerView.adapter as ForecastAdapter).submitList(forecasts)
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
//    private fun requestLocation() {
//        locHelper = LocationHelper(requireActivity())
//        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            locationPermissionLauncher.launch(arrayOf(
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ))
//        } else {
//            fetchLocation()
//        }
//    }
//
//    private fun fetchLocation() {
//        val apiKey = "9a3fd0649e44a6fb8826038be3aa48fc"
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
