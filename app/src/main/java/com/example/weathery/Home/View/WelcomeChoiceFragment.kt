package com.example.weathery.Home.View

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.weathery.R
import com.example.weathery.View.INavFragmaent
import com.example.weathery.Home.View.HomeFragment

class WelcomeChoiceFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.fragment_welcome_choice_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prefs = requireContext()
            .getSharedPreferences("weather_prefs", Context.MODE_PRIVATE)

        view.findViewById<Button>(R.id.btnUseGPS).setOnClickListener {
            prefs.edit()
                .putString("location_method", "GPS")
                .remove("saved_latitude")
                .remove("saved_longitude")
                .apply()
            navigateToHome()
        }

        view.findViewById<Button>(R.id.btnUseMap).setOnClickListener {
            prefs.edit().putString("location_method", "Map").apply()
            // instead of going straight home, first open map chooser
            (requireActivity() as INavFragmaent).navigateTo(MapSelectionFragment(), true)
        }
    }

    private fun navigateToHome() {
        (requireActivity() as INavFragmaent)
            .navigateTo(HomeFragment(), false)
    }
}

