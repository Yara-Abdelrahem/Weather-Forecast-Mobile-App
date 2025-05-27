package com.example.weathery.View

import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.weathery.R
import com.example.weathery.View.ui.Alerts.ShowAlertFragment
import com.example.weathery.View.ui.FavoriteCity.ShowFavoriteFragment
import com.example.weathery.View.ui.home.HomeFragment
import com.example.weathery.View.INavFragmaent
import com.example.weathery.databinding.ActivityHomeBinding
import com.example.weathery.work.LocationHelper
import com.google.android.material.navigation.NavigationView
import androidx.navigation.ui.AppBarConfiguration
import com.example.weathery.View.ui.setting.SettingsFragment

class HomeActivity : AppCompatActivity(), INavFragmaent {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var locHelper: LocationHelper
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1) Register the permission launcher
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { perms ->
            // The callback is handled by LocationHelper, but we can log for debugging
            val granted = perms.all { it.value }
            Log.d("HomeActivity", "Permissions granted: $granted")
        }

        // 2) Set up Toolbar
        setSupportActionBar(binding.appBarHome.toolbar)

        // 3) Initialize LocationHelper with the permission launcher
        locHelper = LocationHelper(this, permissionLauncher)

        // 4) Default fragment
        if (savedInstanceState == null) {
            navigateTo(HomeFragment(), false)
        }

        // 5) Configure Drawer + NavigationView
        val drawerLayout: DrawerLayout = binding.drawerLayout
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_fav_city,
                R.id.nav_alert,
                R.id.nav_setting
            ),
            drawerLayout
        )

        binding.navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> navigateTo(HomeFragment(), true)
                R.id.nav_fav_city -> navigateTo(ShowFavoriteFragment(), true)
                R.id.nav_alert -> navigateTo(ShowAlertFragment(), true)
                R.id.nav_setting -> navigateTo(SettingsFragment(), true)
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun navigateTo(fragment: Fragment, addToBackstack: Boolean) {
        val transaction = supportFragmentManager
            .beginTransaction()
            .replace(binding.appBarHome.contentHome.fragmentContainer.id, fragment)

        if (addToBackstack) transaction.addToBackStack(null)
        transaction.commit()
    }
}

//package com.example.weathery.View
//
//import android.os.Bundle
//import android.util.Log
//import android.view.Menu
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.GravityCompat
//import androidx.drawerlayout.widget.DrawerLayout
//import androidx.fragment.app.Fragment
//import com.example.weathery.R
//import com.example.weathery.View.ui.Alerts.ShowAlertFragment
//import com.example.weathery.View.ui.FavoriteCity.ShowFavoriteFragment
//import com.example.weathery.View.ui.home.HomeFragment
//import com.example.weathery.View.INavFragmaent
//import com.example.weathery.databinding.ActivityHomeBinding
//import com.example.weathery.work.LocationHelper
//import com.google.android.material.navigation.NavigationView
//import androidx.navigation.ui.AppBarConfiguration
//import com.example.weathery.View.ui.setting.SettingsFragment
//
//class HomeActivity : AppCompatActivity(), INavFragmaent {
//
//    private lateinit var binding: ActivityHomeBinding
//    private lateinit var locHelper: LocationHelper
//    private lateinit var appBarConfiguration: AppBarConfiguration
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = ActivityHomeBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        // 1) Set up Toolbar
//        setSupportActionBar(binding.appBarHome.toolbar)
//
//        // 2) Initialize LocationHelper (it registers its own permission launcher internally)
//        locHelper = LocationHelper(this)
//
//        // 3) Default fragment
//        if (savedInstanceState == null) {
//            navigateTo(HomeFragment(), false)
//        }
//
//        // 4) Configure Drawer + NavigationView
//        val drawerLayout: DrawerLayout = binding.drawerLayout
//        appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.nav_home,
//                R.id.nav_fav_city,
//                R.id.nav_alert,
//                R.id.nav_setting
//            ),
//            drawerLayout
//        )
//
//        binding.navView.setNavigationItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.nav_home ->      navigateTo(HomeFragment(), true)
//                R.id.nav_fav_city ->  navigateTo(ShowFavoriteFragment(), true)
//                R.id.nav_alert ->     navigateTo(ShowAlertFragment(), true)
//                R.id.nav_setting -> navigateTo(SettingsFragment(),true)
//            }
//            drawerLayout.closeDrawer(GravityCompat.START)
//            true
//        }
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.home, menu)
//        return true
//    }
//
//    override fun navigateTo(fragment: Fragment, addToBackstack: Boolean) {
//        val transaction = supportFragmentManager
//            .beginTransaction()
//            .replace(binding.appBarHome.contentHome.fragmentContainer.id, fragment)
//
//        if (addToBackstack) transaction.addToBackStack(null)
//        transaction.commit()
//    }
//}
