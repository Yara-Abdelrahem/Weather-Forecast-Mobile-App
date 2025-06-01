package com.example.weathery.Home.View

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.weathery.AlarmAlert.View.AlertFragment
import com.example.weathery.Favorite.View.ShowFavoriteFragment
import com.example.weathery.Home.INavFragmaent
import com.example.weathery.Home.LocationHelper
import com.example.weathery.R
import com.example.weathery.Settings.View.SettingsFragment
import com.example.weathery.databinding.ActivityHomeBinding
import java.util.Locale

class HomeActivity : AppCompatActivity(), INavFragmaent {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var locHelper: LocationHelper
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    fun setLocale(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }

    @RequiresPermission(allOf = [
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ])
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("weather_prefs", MODE_PRIVATE)
        val method = prefs.getString("location_method", null)
        val language = prefs.getString(SettingsFragment.Companion.KEY_LANGUAGE, "English") ?: "English"
        val languageCode = if (language == "English") "en" else "ar"

        setLocale(this,languageCode)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ── 1) register your permission launcher up front ──
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { perms ->
            Log.d("HomeActivity","Permissions: $perms")
            if (perms[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                // re-use LocationHelper to fire your callback
                locHelper.getCurrentLocation{ lat, lon ->
                    navigateTo(HomeFragment(lat, lon, false), false)
                }
            }
        }

        // ── 2) initialize LocationHelper ──
        locHelper = LocationHelper(this, permissionLauncher)

        // ── 3) only once, pick welcome vs home ──
        if (savedInstanceState == null) {
            val prefs = getSharedPreferences("weather_prefs", MODE_PRIVATE)
            val method = prefs.getString("location_method", null)
            val firstScreen: Fragment = when (method) {
                null -> WelcomeChoiceFragment()
                else -> HomeFragment()  // default GPS/Map logic inside the fragment
            }
            navigateTo(firstScreen, false)
        }

        // ── 4) set up toolbar & drawer only after your initial nav ──
        setSupportActionBar(binding.appBarHome.toolbar)
        setupDrawer()
    }

    private fun setupDrawer() {
        val drawer = binding.drawerLayout
        val nav = binding.navView
        // You can wire up AppBarConfiguration if you need it, but for manual nav:
        nav.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home        -> navigateTo(HomeFragment(), true)
                R.id.nav_fav_city    -> navigateTo(ShowFavoriteFragment(), true)
                R.id.nav_alert       -> navigateTo(AlertFragment(), true)
                R.id.nav_setting     -> navigateTo(SettingsFragment(), true)
            }
            drawer.closeDrawer(GravityCompat.START)
            true
        }
    }

    override fun navigateTo(fragment: Fragment, addToBackstack: Boolean) {
        val tx = supportFragmentManager.beginTransaction()
            .replace(binding.appBarHome.contentHome.fragmentContainer.id, fragment)
        if (addToBackstack) tx.addToBackStack(null)
        tx.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home, menu)
        return true
    }
}