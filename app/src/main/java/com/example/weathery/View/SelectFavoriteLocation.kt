package com.example.weathery.View

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.weathery.R
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.osmdroid.config.*

class SelectFavoriteLocationFragment : Fragment() {

    private lateinit var mapView: MapView
    private lateinit var btnSelectLocation: Button
    private var selectedGeoPoint: GeoPoint? = null
    private var selectedCityName: String = "Unknown"
    private lateinit var geoPoint : GeoPoint
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_select_favorite_location, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Configuration.getInstance().load(
            requireContext(),
            PreferenceManager.getDefaultSharedPreferences(requireContext())
        )

        mapView = view.findViewById(R.id.mapView)
        btnSelectLocation = view.findViewById(R.id.btnSelectLocation)

        mapView.setMultiTouchControls(true)
        mapView.controller.setZoom(15.0)
        mapView.controller.setCenter(GeoPoint(30.0444, 31.2357)) // Centered on Cairo, Egypt

        mapView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val projection = mapView.projection
                val geoPoint = projection.fromPixels(event.x.toInt(), event.y.toInt()) as GeoPoint
                selectedGeoPoint = geoPoint

                // Add marker
                val marker = Marker(mapView)
                marker.position = geoPoint
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                mapView.overlays.clear()
                mapView.overlays.add(marker)
                mapView.invalidate()
                // Perform reverse geocoding
                getCityName(geoPoint.latitude, geoPoint.longitude)

                true
            } else {
                false
            }
        }
        btnSelectLocation.setOnClickListener {

            selectedGeoPoint?.let { geoPoint ->
                Log.i("Locationnnnn", "$selectedCityName ----- ${geoPoint.latitude}----${geoPoint.longitude}")
                (activity as? FavoriteActivity)?.onLocationPicked(geoPoint.latitude ,geoPoint.longitude , selectedCityName)
                parentFragmentManager.popBackStack()

            } ?: run {
                Toast.makeText(requireContext(), "Please select a location on the map.", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun getCityName(lat: Double, lon: Double) {
        val url =
            "https://nominatim.openstreetmap.org/reverse?format=json&lat=$lat&lon=$lon&zoom=10&addressdetails=1"

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val address = response.getJSONObject("address")
                    selectedCityName = address.optString("city", address.optString("town", "Unknown"))
//                    Toast.makeText(requireContext(), "City: $selectedCityName", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Parsing error", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )

        val queue = Volley.newRequestQueue(requireContext())
        queue.add(request)
    }
}
