package com.example.weathery.Settings.View

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weathery.Home.View.MapSelectionFragment
import com.example.weathery.View.INavFragmaent
import com.example.weathery.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val prefs = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        // Load current preferences and set radio buttons
        val temperatureUnit = prefs.getString(KEY_TEMPERATURE_UNIT, "Kelvin") ?: "Kelvin"
        when (temperatureUnit) {
            "Kelvin" -> binding.radioKelvin.isChecked = true
            "Celsius" -> binding.radioCelsius.isChecked = true
            "Fahrenheit" -> binding.radioFahrenheit.isChecked = true
        }

        val windSpeedUnit = prefs.getString(KEY_WIND_SPEED_UNIT, "meter/sec") ?: "meter/sec"
        when (windSpeedUnit) {
            "meter/sec" -> binding.radioMeterSec.isChecked = true
            "miles/hour" -> binding.radioMilesHour.isChecked = true
        }

        val locationMethod = prefs.getString(KEY_LOCATION_METHOD, "GPS") ?: "GPS"
        when (locationMethod) {
            "GPS" -> binding.radioGps.isChecked = true
            "Map" -> binding.radioMap.isChecked = true
        }

        // Load current language preference
        val language = prefs.getString(KEY_LANGUAGE, "English") ?: "English"
        when (language) {
            "English" -> binding.radioEnglish.isChecked = true
            "Arabic" -> binding.radioArabic.isChecked = true
        }

        // Set listeners to save changes to SharedPreferences
        binding.temperatureUnitGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedUnit = when (checkedId) {
                binding.radioKelvin.id -> "Kelvin"
                binding.radioCelsius.id -> "Celsius"
                binding.radioFahrenheit.id -> "Fahrenheit"
                else -> "Kelvin"
            }
            prefs.edit().putString(KEY_TEMPERATURE_UNIT, selectedUnit).apply()
        }

        binding.windSpeedUnitGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedUnit = when (checkedId) {
                binding.radioMeterSec.id -> "meter/sec"
                binding.radioMilesHour.id -> "miles/hour"
                else -> "meter/sec"
            }
            prefs.edit().putString(KEY_WIND_SPEED_UNIT, selectedUnit).apply()
        }

        binding.locationMethodGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedMethod = when (checkedId) {
                binding.radioGps.id -> "GPS"
                binding.radioMap.id -> "Map"
                else -> "GPS"
            }
            prefs.edit().putString(KEY_LOCATION_METHOD, selectedMethod).apply()
            if (selectedMethod == "Map") {
                val activity = requireActivity() as INavFragmaent
                activity.navigateTo(MapSelectionFragment(), false)
            }        }
        binding.languageGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedLanguage = when (checkedId) {
                binding.radioEnglish.id -> "English"
                binding.radioArabic.id -> "Arabic"
                else -> "English"
            }
            prefs.edit().putString(KEY_LANGUAGE, selectedLanguage).apply()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val PREF_NAME = "weather_prefs"
        const val KEY_TEMPERATURE_UNIT = "temperature_unit"
        const val KEY_WIND_SPEED_UNIT = "wind_speed_unit"
        const val KEY_LOCATION_METHOD = "location_method"
        const val KEY_LANGUAGE = "language"
    }
}