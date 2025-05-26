package com.example.weathery.View

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.weathery.Model.FavoriteCity
import com.example.weathery.Model.FavoriteCityRepositry
import com.example.weathery.Model.LocalFavorityCityDatasource
import com.example.weathery.R
import com.example.weathery.View.ui.FavoriteCity.SelectFavoriteLocationFragment
import com.example.weathery.View.ui.FavoriteCity.ShowFavoriteFragment
import com.example.weathery.ViewModel.FavoriteCityViewModel
import com.example.weathery.WeatherDatabase
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {
    lateinit var favcity_viewmode :FavoriteCityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        var favcitudao = WeatherDatabase.getDatabase(this).weatherDao()

        favcity_viewmode = FavoriteCityViewModel(
            FavoriteCityRepositry(
                LocalFavorityCityDatasource(favcitudao)
            )
        )
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, ShowFavoriteFragment())
                .commit()
        }
    }

    fun openMapPicker() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, SelectFavoriteLocationFragment())
            .addToBackStack(null)
            .commit()
    }

      fun onLocationPicked(lat: Double, lon: Double, city: String) {
        // Remove the map fragment
        supportFragmentManager.popBackStack()
        // Add to list and notify adapter
        val label = "$city ($lat, $lon)"
        lifecycleScope.launch{
            favcity_viewmode.insertFavCity(FavoriteCity(city_name = city , city_lat = lat, city_lon = lon))
        }
        Toast.makeText(this, "Location added to favorites. $label", Toast.LENGTH_SHORT).show()
    }
}



