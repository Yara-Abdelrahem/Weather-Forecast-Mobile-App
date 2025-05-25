package com.example.weathery.View

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.weathery.R
import com.example.weathery.Service.AlarmService
import java.util.jar.Manifest

class AlertActivity : AppCompatActivity() {
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_alert_container, ShowAlertFragment())
                .commit()
        }
//        if (hasLocationPermissions()) {
//            startService(Intent(this, AlarmService::class.java))
//        } else {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                ),
//                LOCATION_PERMISSION_REQUEST_CODE
//            )
//        }

//        if (!hasLocationPermissions()) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                ),
//                LOCATION_PERMISSION_REQUEST_CODE
//            )
//        } else {
//            // Permissions already granted — you can start your location-based services
//        }

    }

//    private fun hasLocationPermissions(): Boolean {
//        return ContextCompat.checkSelfPermission(
//            this,
//            Manifest.permission.ACCESS_FINE_LOCATION
//        ) == PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                ) == PackageManager.PERMISSION_GRANTED
//    }


    fun select_Alert() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_alert_container, SelectTimeFragment())
            .addToBackStack(null)
            .commit()
    }
    fun finsh_set_alert(){
        // Remove the map fragment
        supportFragmentManager.popBackStack()
        Toast.makeText(this, "Alert added.", Toast.LENGTH_SHORT).show()

    }
}
