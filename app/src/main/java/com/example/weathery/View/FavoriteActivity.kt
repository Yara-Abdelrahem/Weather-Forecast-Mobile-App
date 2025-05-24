package com.example.weathery.View

import android.annotation.SuppressLint
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.MotionEvent
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weathery.R
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class FavoriteActivity : AppCompatActivity(), OnLocationPickedListener {
    private lateinit var rvFavorites: RecyclerView
    private val favoriteLocations = mutableListOf<String>()
    private lateinit var adapter: SimpleStringAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        rvFavorites = findViewById(R.id.rvFavorites)
        rvFavorites.layoutManager = LinearLayoutManager(this)
        adapter = SimpleStringAdapter(favoriteLocations)
        rvFavorites.adapter = adapter

        findViewById<Button>(R.id.btnAddToFavorites).setOnClickListener {
            // Launch the map picker fragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_location_container, SelectFavoriteLocationFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onLocationPicked(lat: Double, lon: Double, city: String) {
        // Remove the map fragment
        supportFragmentManager.popBackStack()

        // Add to list and notify adapter
        val label = "$city ($lat, $lon)"
        favoriteLocations.add(label)
        adapter.notifyItemInserted(favoriteLocations.size - 1)
        Toast.makeText(this, "Location added to favorites.", Toast.LENGTH_SHORT).show()
    }
}



//class FavoriteActivity : AppCompatActivity() {
//    private lateinit var mapView: MapView
//    @SuppressLint("ClickableViewAccessibility")
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_favorite)
//        Configuration.getInstance().load(applicationContext, PreferenceManager.getDefaultSharedPreferences(applicationContext))
//        mapView = findViewById(R.id.mapView)
//        mapView.setMultiTouchControls(true)
//        mapView.controller.setZoom(15.0)
//        mapView.controller.setCenter(GeoPoint(30.0444, 31.2357)) // Centered on Cairo, Egypt
//
//        mapView.setOnTouchListener { _, event ->
//            if (event.action == MotionEvent.ACTION_UP) {
//                val projection = mapView.projection
//                val geoPoint = projection.fromPixels(event.x.toInt(), event.y.toInt()) as GeoPoint
//                val latitude = geoPoint.latitude
//                val longitude = geoPoint.longitude
//                // Add marker
//                val marker = Marker(mapView)
//                marker.position = geoPoint
//                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
//                mapView.overlays.clear()
//                mapView.overlays.add(marker)
//                mapView.invalidate()
//                Log.i("Locattionnnnnnnnnnnnnnnnnnnn" , "$longitude ----------- $latitude")
//                true
//            } else {
//                false
//            }
//        }
//    }
//}