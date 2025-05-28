package com.example.weathery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.weathery.AlarmAlert.AlarmActivity
import com.example.weathery.View.INavFragmaent
import com.example.weathery.databinding.ActivityMainBinding
import com.example.weathery.Home.View.WelcomeChoiceFragment
import com.example.weathery.View.HomeActivity
import com.example.weathery.View.ui.home.HomeFragment

class MainActivity : AppCompatActivity(), INavFragmaent {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // On first startup decide which fragment to show
        if (savedInstanceState == null) {
            val prefs = getSharedPreferences("weather_prefs", Context.MODE_PRIVATE)
            val method = prefs.getString("location_method", null)
            if (method == null) {
                navigateTo(WelcomeChoiceFragment(), false)
            } else {
                val intent = Intent(this, HomeActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                startActivity(intent)
            }
        }
    }

    override fun navigateTo(fragment: Fragment, addToBackstack: Boolean) {
        val tx = supportFragmentManager.beginTransaction()
            .replace(binding.welcomeContainer.id, fragment)
        if (addToBackstack) tx.addToBackStack(null)
        tx.commit()
    }
}
