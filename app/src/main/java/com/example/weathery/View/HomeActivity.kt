package com.example.weathery.View

import android.os.Bundle
import android.view.Menu
import com.example.weathery.R
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.weathery.View.ui.Alerts.ShowAlertFragment
import com.example.weathery.View.ui.FavoriteCity.FavoriteFragment
import com.example.weathery.View.ui.FavoriteCity.ShowFavoriteFragment
import com.example.weathery.View.ui.home.HomeFragment
import com.example.weathery.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() , INavFragmaent{

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.appBarHome.contentHome.fragmentContainer
        setSupportActionBar(binding.appBarHome.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_fav_city, R.id.nav_alert, R.id.nav_setting
            ), drawerLayout
        )

        binding.navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {

                    navigateTo(HomeFragment(),true)
                }

                R.id.nav_fav_city -> {

                    navigateTo(ShowFavoriteFragment(),true)
                }

                R.id.nav_alert -> {
                    navigateTo(ShowAlertFragment(),true)
                }

                R.id.nav_setting -> {
                    // Handle Settings click
                }
            }

            // Close drawer after selection
            val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
            drawer.closeDrawer(GravityCompat.START)
            true
        }
        navigateTo(HomeFragment(),false)
      //  setupActionBarWithNavController(navController, appBarConfiguration)
    //    navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }
    public override  fun navigateTo(fragment: Fragment, addToBackstack: Boolean) {
        val transaction =
            getSupportFragmentManager()
                .beginTransaction()
                .replace(binding.appBarHome.contentHome.fragmentContainer.getId(), fragment)

        if (addToBackstack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

}