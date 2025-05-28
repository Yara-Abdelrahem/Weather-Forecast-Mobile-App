package com.example.weathery.Home.View

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.weathery.AlarmAlert.AlarmActivity
import com.example.weathery.R
import com.example.weathery.View.HomeActivity
import com.example.weathery.View.INavFragmaent
import com.example.weathery.View.ui.home.HomeFragment
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Overlay

class MapSelectionFragment : Fragment() {

    private lateinit var mapView: MapView
    private lateinit var btnConfirm: Button
    private var chosen: GeoPoint? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Load osmdroid configuration
        Configuration.getInstance().load(
            requireContext(),
            PreferenceManager.getDefaultSharedPreferences(requireContext())
        )
        return inflater.inflate(R.layout.fragment_map_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = view.findViewById(R.id.mapView)
        btnConfirm = view.findViewById(R.id.btnConfirm)

        // Basic map setup
        mapView.setMultiTouchControls(true)
        mapView.controller.setZoom(15.0)
        mapView.controller.setCenter(GeoPoint(30.0444, 31.2357)) // Cairo fallback

        // Listen for taps
        mapView.overlays.add(object : Overlay() {
            override fun onSingleTapConfirmed(e: MotionEvent, mapView: MapView): Boolean {
                val geo = mapView.projection.fromPixels(e.x.toInt(), e.y.toInt()) as GeoPoint
                chosen = geo
                // clear old, add new marker
                mapView.overlays.filterIsInstance<Marker>().forEach { mapView.overlays.remove(it) }
                val marker = Marker(mapView).apply {
                    position = geo
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                }
                mapView.overlays.add(marker)
                mapView.invalidate()
                return true
            }
        })

        btnConfirm.setOnClickListener {
            chosen?.let { pt ->
                // save into prefs
                val prefs = requireContext()
                    .getSharedPreferences("weather_prefs", Context.MODE_PRIVATE)
                prefs.edit()
                    .putFloat("saved_latitude", pt.latitude.toFloat())
                    .putFloat("saved_longitude", pt.longitude.toFloat())
                    .apply()
                    Log.d("MapSelectionFragment", "Location saved: ${pt.latitude}, ${pt.longitude}")
                // navigate back to HomeFragment
                val intent = Intent(requireContext(), HomeActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                startActivity(intent)
            } ?: run {
                Toast.makeText(requireContext(),
                    "Please tap on the map to select a location first",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }
}
