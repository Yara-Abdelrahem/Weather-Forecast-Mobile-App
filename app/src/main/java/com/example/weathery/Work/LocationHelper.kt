package com.example.weathery.work

import android.Manifest
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.MainThread
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

class LocationHelper(
    private val activity: AppCompatActivity,
    private val permissionLauncher: ActivityResultLauncher<Array<String>>
) {

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(activity)
    }

    private var locationCallback: LocationCallback? = null
    private var pendingLocationCallback: ((Double, Double) -> Unit)? = null

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    @MainThread
    fun requestLocation(onResult: (lat: Double, lon: Double) -> Unit) {
        if (hasLocationPermissions()) {
            startLocationUpdates(onResult)
        } else {
            // Store callback for later use after permission granted
            pendingLocationCallback = onResult
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    fun stop() {
        locationCallback?.let { fusedLocationClient.removeLocationUpdates(it) }
    }

    private fun hasLocationPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            activity, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    activity, Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun startLocationUpdates(onResult: (lat: Double, lon: Double) -> Unit) {
        val request = LocationRequest.Builder(0L)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setMaxUpdates(1)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val loc = result.lastLocation
                if (loc != null) {
                    onResult(loc.latitude, loc.longitude)
                }
                fusedLocationClient.removeLocationUpdates(this)
            }
        }

        fusedLocationClient.requestLocationUpdates(
            request,
            locationCallback!!,
            Looper.getMainLooper()
        )
    }
}
//package com.example.weathery.work
//
//import android.Manifest
//import android.content.pm.PackageManager
//import android.os.Looper
//import android.util.Log
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.annotation.MainThread
//import androidx.annotation.RequiresPermission
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContentProviderCompat.requireContext
//import com.google.android.gms.location.*
//
//class LocationHelper(private val activity: AppCompatActivity) {
//
//    private val fusedLocationClient by lazy {
//        LocationServices.getFusedLocationProviderClient(activity)
//    }
//
//    private var locationCallback: LocationCallback? = null
//    private var pendingLocationCallback: ((Double, Double) -> Unit)? = null
//
//    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
//    @MainThread
//    fun requestLocation(onResult: (lat: Double, lon: Double) -> Unit) {
//        if (hasLocationPermissions()) {
//            startLocationUpdates(onResult)
//        } else {
//            // Store callback for later use after permission granted
//            pendingLocationCallback = onResult
//            permissionLauncher.launch(
//                arrayOf(
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                )
//            )
//        }
//    }
//    fun stop() {
//        locationCallback?.let { fusedLocationClient.removeLocationUpdates(it) }
//    }
//    private fun hasLocationPermissions(): Boolean {
//        return ActivityCompat.checkSelfPermission(
//            activity, Manifest.permission.ACCESS_FINE_LOCATION
//        ) == PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(
//                    activity, Manifest.permission.ACCESS_COARSE_LOCATION
//                ) == PackageManager.PERMISSION_GRANTED
//    }
//
//    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
//    private fun startLocationUpdates(onResult: (lat: Double, lon: Double) -> Unit) {
//        val request = LocationRequest.Builder(0L)
//            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
//            .setMaxUpdates(1)
//            .build()
//
//        locationCallback = object : LocationCallback() {
//            override fun onLocationResult(result: LocationResult) {
//                val loc = result.lastLocation
//                if (loc != null) {
//                    onResult(loc.latitude, loc.longitude)
//                }
//                fusedLocationClient.removeLocationUpdates(this)
//            }
//        }
//
//        fusedLocationClient.requestLocationUpdates(
//            request,
//            locationCallback!!,
//            Looper.getMainLooper()
//        )
//    }
//
//    // ——— Activity Result API for Permissions ——— //
//    private val permissionLauncher =
//        activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { perms ->
//            val granted = perms.all { it.value }
//            if (granted) {
//                pendingLocationCallback?.let { callback ->
//                    // re-trigger location request
//                    requestLocation(callback)
//                    pendingLocationCallback = null
//                }
//            } else {
//                Log.e("LocationHelper", "Location permission denied")
//            }
//        }
//}


//package com.example.weathery.work
//
//import android.Manifest
//import android.R
//import android.content.Context
//import android.content.Context.LOCATION_SERVICE
//import android.content.pm.PackageManager
//import android.location.Location
//import android.location.LocationManager
//import android.os.Looper
//import android.util.Log
//import android.widget.Toast
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.annotation.MainThread
//import androidx.annotation.RequiresPermission
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import androidx.core.content.ContextCompat.getSystemService
//import com.google.android.gms.location.*
//import java.security.Permission
//
//
//// Better implementation
//class LocationHelper(private val activity: AppCompatActivity) {
//    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
//     var localPermissionCode = 1
//    fun getLocation() {
//        if (ActivityCompat.checkSelfPermission(
//                activity,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                activity,
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                REQUEST_CODE
//            )
//            return
//        }
//        fusedLocationClient.lastLocation
//            .addOnSuccessListener { location ->
//                location?.let {
//                    Log.d("Location", "Lat: ${it.latitude}, Lng: ${it.longitude}")
//                }
//            }
//    }
//
//    companion object {
//        private const val REQUEST_CODE = 1001
//    }
//}



//class LocationHelper(private val activity: AppCompatActivity) : LocationListener {    private lateinit var locationManager: LocationManager
//    private var localpermissioncode= 1
//    fun getLocation(){
//        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        if ((ContextCompat.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_lOCATION)!= PackageManager.PERMISSION_GRANTED)){
//            ActivityCompat.requestPermissions(activity , arrayOf(android.Manifest.permission.ACCESS_FINE_lOCATION),localpermissioncode)
//        }
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5F, this)
//    }
//
//    override fun onLocationChanged(p0: Location) {
//        TODO("Not yet implemented")
//    }
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == localPermissionCode) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission granted, proceed with location updates
//            } else {
//                Toast.makeText(this, "GPS permission denied", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//}


//class LocationHelper(private val activity: AppCompatActivity) {
//
//    private val fusedLocationClient by lazy {
//        LocationServices.getFusedLocationProviderClient(activity)
//    }
//
//    private var locationCallback: LocationCallback? = null
//    private var pendingLocationCallback: ((Double, Double) -> Unit)? = null
//
//    fun requestLocation(onResult: (lat: Double, lon: Double) -> Unit) {
//        if (hasLocationPermissions()) {
//            if (ActivityCompat.checkSelfPermission(
//                    activity,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
//                    activity,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return
//            }
//            startLocationUpdates(onResult)
//        } else {
//            // Store callback for later use after permission granted
//            pendingLocationCallback = onResult
//            permissionLauncher.launch(
//                arrayOf(
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                )
//            )
//        }
//    }
//
//    /**
//     * 2️⃣ In Activity.onDestroy(), call this to avoid memory leaks.
//     */
//    fun stop() {
//        locationCallback?.let { fusedLocationClient.removeLocationUpdates(it) }
//    }
//
//    // ——— Helpers ——— //
//
//    private fun hasLocationPermissions(): Boolean {
//        return ActivityCompat.checkSelfPermission(
//            activity, Manifest.permission.ACCESS_FINE_LOCATION
//        ) == PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(
//                    activity, Manifest.permission.ACCESS_COARSE_LOCATION
//                ) == PackageManager.PERMISSION_GRANTED
//    }
//
//    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
//    private fun startLocationUpdates(onResult: (lat: Double, lon: Double) -> Unit) {
//        val request = LocationRequest.Builder(0L)
//            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
//            .setMaxUpdates(1)
//            .build()
//
//        locationCallback = object : LocationCallback() {
//            override fun onLocationResult(result: LocationResult) {
//                val loc = result.lastLocation
//                if (loc != null) {
//                    onResult(loc.latitude, loc.longitude)
//                }
//                fusedLocationClient.removeLocationUpdates(this)
//            }
//        }
//
//        fusedLocationClient.requestLocationUpdates(
//            request,
//            locationCallback!!,
//            Looper.getMainLooper()
//        )
//    }
//
//    // ——— Activity Result API for Permissions ——— //
//    private val permissionLauncher =
//        activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { perms ->
//            val granted = perms.all { it.value }
//            if (granted) {
//                pendingLocationCallback?.let { callback ->
//                    // re-trigger location request
//                    requestLocation(callback)
//                    pendingLocationCallback = null
//                }
//            } else {
//                Log.e("LocationHelper", "Location permission denied")
//            }
//        }
//}

//class locatio(private val activity: AppCompatActivity){
//    private final val LOCATION_PERMISSION_CODE : Int  = 1
//    private lateinit var fusedLocationClient: FusedLocationProviderClient
//
//    fun get_loc(){
//        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
//            ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//
//            setupLocationPermissions { long, lat ->
//                Log.i( "latttttttttttttt" ,  long.toString())
//                Log.i( "latttttttttttttt" , lat.toString())
//
//                if (long != null && lat != null) {
//                    val geocoder = android.location.Geocoder(activity)
//                    val addressList = geocoder.getFromLocation(lat, long, 1)
//
//                    if (!addressList.isNullOrEmpty()) {
//                        val address = addressList[0].getAddressLine(0)
//
//                        Log.i("Get addresssssssssss" , address)
//
//                    } else {
//
//                        Log.e("Probleeeeeeeeeem" ,"Address not found")
//
//                    }
//                } else {
//                    Log.e("Probleeeeeeeeeem" ,"Location unavailable")
//                }
//            }
//        } else {
//            ActivityCompat.requestPermissions(
//                activity,
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
//                LOCATION_PERMISSION_CODE
//            )
//        }
//    }
//
//    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == LOCATION_PERMISSION_CODE &&
//            grantResults.isNotEmpty() &&
//            grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
//            setupLocationPermissions { long, lat ->
//                Log.i( "latttttttttttttt" ,  long.toString())
//                Log.i( "latttttttttttttt" , lat.toString())
//            }
//        }
//    }
//
//    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
//    fun setupLocationPermissions(onResultCB : (Double,Double) -> Unit){
//        val locationRequest = LocationRequest.Builder(0).apply {
//            setPriority(Priority.PRIORITY_HIGH_ACCURACY)
//        }.build()
//
//        val locationCallback = object : LocationCallback() {
//            override fun onLocationResult(locationResult: LocationResult)
//            {
//                val longit : Double = locationResult.lastLocation?.longitude ?: 0.0
//                val latit : Double = locationResult.lastLocation?.latitude ?: 0.0
//                onResultCB(longit,latit)
//            }
//        }
//
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
//        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
//        fusedLocationClient.requestLocationUpdates(
//            locationRequest,
//            locationCallback,
//            Looper.myLooper()
//        )
//    }
//}


