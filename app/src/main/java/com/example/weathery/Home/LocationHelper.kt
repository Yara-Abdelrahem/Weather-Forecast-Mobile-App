package com.example.weathery.Home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Looper
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

class LocationHelper(
    private val activity: AppCompatActivity,
    private val permissionLauncher: ActivityResultLauncher<Array<String>>
) {
    private val client by lazy { LocationServices.getFusedLocationProviderClient(activity) }

    /**
     * Fetch one high-accuracy location.  Will request permissions if needed.
     */
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun getCurrentLocation(onResult: (lat: Double, lon: Double) -> Unit) {
        if (hasPermission()) {
            fetchOnce(onResult)
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun hasPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            activity, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    activity, Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun fetchOnce(onResult: (lat: Double, lon: Double) -> Unit) {
        val req = LocationRequest.Builder(0L)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setMaxUpdates(1)
            .build()

        val cb = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let {
                    onResult(it.latitude, it.longitude)
                }
                client.removeLocationUpdates(this)
            }
        }

        client.requestLocationUpdates(req, cb, Looper.getMainLooper())
    }
}
