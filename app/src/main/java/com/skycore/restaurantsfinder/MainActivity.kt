package com.skycore.restaurantsfinder

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.skycore.restaurantsfinder.helper.DistanceMappingHelper
import com.skycore.restaurantsfinder.helper.DistanceMappingHelper.INITIAL_RADIUS_IN_METERS
import com.skycore.restaurantsfinder.helper.LocationPermissionHelper
import com.skycore.restaurantsfinder.ui.RestaurantsListAdapter
import com.skycore.restaurantsfinder.ui.RestaurantsViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference


class MainActivity : AppCompatActivity() {

    private lateinit var locationManager: LocationManager
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val viewModel: RestaurantsViewModel by viewModels()
    private var adapter: RestaurantsListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        adapter = RestaurantsListAdapter()
        activity_main_rv_restaurants.layoutManager = LinearLayoutManager(this)
        activity_main_rv_restaurants.adapter = adapter
        seekbar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                Log.d("seekbar", "seekbar progress: $progress")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                Log.d("seekbar", "seekbar touch started!")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                viewModel.setRadiusData(DistanceMappingHelper.mapProgressBarDistance(seekbar.progress))
            }
        })

        viewModel.radiusData.observe(this, Observer {
            fetchRestaurantsData()
        })

        viewModel.currentLocationData.observe(this, Observer {
            fetchRestaurantsData()
        })
    }

    private fun fetchRestaurantsData() {
        lifecycleScope.launch {
            val radius = if (viewModel.radiusData.value == null) INITIAL_RADIUS_IN_METERS else viewModel.radiusData.value!!
            if (viewModel.currentLocationData.value != null) {
                viewModel.fetchRestaurantsData(
                    viewModel.currentLocationData.value!!.latitude,
                    viewModel.currentLocationData.value!!.longitude,
                    radius
                ).observe(this@MainActivity, Observer {
                    lifecycleScope.launch {
                        adapter?.submitData(it)
                    }
                })
            }
        }
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
                    viewModel.setLocationData(it)
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
