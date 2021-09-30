package com.skycore.restaurantsfinder

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.skycore.restaurantsfinder.helper.LocationPermissionHelper
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference
import android.content.Intent
import android.provider.Settings
import android.widget.Toast

import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener


class MainActivity : AppCompatActivity() {

    private lateinit var locationManager: LocationManager
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        seekbar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
               Log.d("seekbar", "seekbar progress: $progress")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                Log.d("seekbar", "seekbar touch started!")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                Log.d("seekbar", "seekbar touch stopped! & value: " +    (seekBar.progress * 5000)/100)

            }
        })
    }

    override fun onResume() {
        super.onResume()
        checkSettings()
    }

    private fun checkSettings() {
        if (!LocationPermissionHelper.isLocationPermissionGranted(WeakReference(this))) {
            Snackbar.make(container, "Require Location Permission!", Snackbar.LENGTH_INDEFINITE).setAction("REQUEST") {
                LocationPermissionHelper.requestLocationPermission(WeakReference(this))
            }.show()
        } else {
            checkGpsSettings()
        }
    }

    @SuppressLint("MissingPermission")
    private fun checkGpsSettings() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            fusedLocationClient.lastLocation.addOnSuccessListener {
                it?.let {

                }
            }
        } else {
            Snackbar.make(container, "Require GPS", Snackbar.LENGTH_INDEFINITE).setAction("Turn on") {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }.show();
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
            when (requestCode) {
                LocationPermissionHelper.MY_PERMISSIONS_REQUEST_LOCATION -> {
                    checkGpsSettings()
                }
            }
        }
    }
}
