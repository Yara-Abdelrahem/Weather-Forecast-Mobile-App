package com.example.weathery.View

import android.os.Bundle
import android.util.Log
import android.view.Menu
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

class HomeActivity : AppCompatActivity(), INavFragmaent {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var locHelper: LocationHelper
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1) Set up Toolbar
        setSupportActionBar(binding.appBarHome.toolbar)

        // 2) Initialize LocationHelper (it registers its own permission launcher internally)
        locHelper = LocationHelper(this)

        // 3) Default fragment
        if (savedInstanceState == null) {
            navigateTo(HomeFragment(), false)
        }

        // 4) Configure Drawer + NavigationView
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
                R.id.nav_home ->      navigateTo(HomeFragment(), true)
                R.id.nav_fav_city ->  navigateTo(ShowFavoriteFragment(), true)
                R.id.nav_alert ->     navigateTo(ShowAlertFragment(), true)
                R.id.nav_setting -> {
                    // TODO: handle Settings click
                }
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
//import androidx.activity.result.ActivityResultLauncher
//import androidx.activity.result.contract.ActivityResultContracts
//import com.example.weathery.R
//import com.google.android.material.snackbar.Snackbar
//import com.google.android.material.navigation.NavigationView
//import androidx.navigation.findNavController
//import androidx.navigation.ui.AppBarConfiguration
//import androidx.navigation.ui.navigateUp
//import androidx.navigation.ui.setupActionBarWithNavController
//import androidx.navigation.ui.setupWithNavController
//import androidx.drawerlayout.widget.DrawerLayout
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.GravityCompat
//import androidx.fragment.app.Fragment
//import com.example.weathery.View.ui.Alerts.ShowAlertFragment
//import com.example.weathery.View.ui.FavoriteCity.FavoriteFragment
//import com.example.weathery.View.ui.FavoriteCity.ShowFavoriteFragment
//import com.example.weathery.View.ui.home.HomeFragment
//import com.example.weathery.databinding.ActivityHomeBinding
//import com.example.weathery.work.LocationHelper
//
//class HomeActivity : AppCompatActivity() , INavFragmaent{
//
//    private lateinit var appBarConfiguration: AppBarConfiguration
//    private lateinit var binding: ActivityHomeBinding
//    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = ActivityHomeBinding.inflate(layoutInflater)
//
//        setContentView(binding.root)
//
//        binding.appBarHome.contentHome.fragmentContainer
//        setSupportActionBar(binding.appBarHome.toolbar)
//
//        val drawerLayout: DrawerLayout = binding.drawerLayout
//        val navView: NavigationView = binding.navView
//
//        // Register the permission launcher in onCreate, before the activity is STARTED
//        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { perms ->
//            val granted = perms.all { it.value }
//            if (granted) {
//                // Handle permission granted, but we'll let LocationHelper handle the callback
//            } else {
//                Log.e("HomeActivity", "Location permission denied")
//            }
//        }
//
//        // Initialize HomeFragment with the permission launcher
//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, HomeFragment())
//                .commit()
//        }
//
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.nav_home, R.id.nav_fav_city, R.id.nav_alert, R.id.nav_setting
//            ), drawerLayout
//        )
//
//        binding.navView.setNavigationItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.nav_home -> {
//
//                    navigateTo(HomeFragment(),true)
//                }
//
//                R.id.nav_fav_city -> {
//
//                    navigateTo(ShowFavoriteFragment(),true)
//                }
//
//                R.id.nav_alert -> {
//                    navigateTo(ShowAlertFragment(),true)
//                }
//
//                R.id.nav_setting -> {
//                    // Handle Settings click
//                }
//            }
//
//            // Close drawer after selection
//            val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
//            drawer.closeDrawer(GravityCompat.START)
//            true
//        }
//        navigateTo(HomeFragment(),false)
//      //  setupActionBarWithNavController(navController, appBarConfiguration)
//    //    navView.setupWithNavController(navController)
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.home, menu)
//        return true
//    }
//    public override  fun navigateTo(fragment: Fragment, addToBackstack: Boolean) {
//        val transaction =
//            getSupportFragmentManager()
//                .beginTransaction()
//                .replace(binding.appBarHome.contentHome.fragmentContainer.getId(), fragment)
//
//        if (addToBackstack) {
//            transaction.addToBackStack(null)
//        }
//        transaction.commit()
//    }
//
//}