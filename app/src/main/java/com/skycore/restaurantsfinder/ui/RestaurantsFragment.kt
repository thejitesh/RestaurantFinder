package com.skycore.restaurantsfinder.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import com.skycore.restaurantsfinder.R
import com.skycore.restaurantsfinder.helper.DistanceMappingHelper
import com.skycore.restaurantsfinder.helper.LocationHelper
import kotlinx.android.synthetic.main.fragment_restaurants_list.*
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference


class RestaurantsFragment : Fragment(R.layout.fragment_restaurants_list) {

    private lateinit var locationManager: LocationManager
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val viewModel: RestaurantsViewModel by viewModels()
    private var adapter: RestaurantsListAdapter? = null
    private var isLocationListenerAdded = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setUpLocationSettings()
        populateRestaurantsList()
        handleDistanceSeekBar()
        swiperefresh.setOnRefreshListener {
            fetchRestaurantsData()
        }
        observeDataChange()
        checkSettings()
    }

    override fun onResume() {
        super.onResume()
        checkSettings()
    }

    private fun setUpLocationSettings() {
        locationManager = requireContext().getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    private fun handleDistanceSeekBar() {
        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                tv_radius_distance.text = DistanceMappingHelper.formatDistance(progress, WeakReference(requireContext()))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                viewModel.setRadiusData(DistanceMappingHelper.mapProgressBarDistance(seekbar.progress))
            }
        })
    }

    private fun populateRestaurantsList() {
        adapter = RestaurantsListAdapter()
        handleRestaurantsAdapterLoadingState()
        activity_main_rv_restaurants.layoutManager = LinearLayoutManager(requireContext())
        activity_main_rv_restaurants.adapter = adapter
        setUpDecorationForRestaurantsList()
    }

    private fun setUpDecorationForRestaurantsList() {
        val dividerItemDecoration = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.shape_verticle_space)
        if (drawable != null) {
            dividerItemDecoration.setDrawable(drawable)
        }
        activity_main_rv_restaurants.addItemDecoration(dividerItemDecoration)
    }

    private fun handleRestaurantsAdapterLoadingState() {
        adapter?.addLoadStateListener { loadState ->
            when {
                loadState.refresh is LoadState.Loading -> {
                    //loading complete data set
                    progressBar.visibility = View.VISIBLE
                    progressBarAppend.visibility = View.GONE
                }
                loadState.append is LoadState.Loading -> {
                    //on demand loading - [pagination]
                    progressBarAppend.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
                else -> {
                    progressBar.visibility = View.GONE
                    progressBarAppend.visibility = View.GONE
                    swiperefresh.isRefreshing = false
                    val errorState = when {
                        loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                        loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                        loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                        else -> null
                    }
                    errorState?.let {
                        Toast.makeText(requireContext(), it.error.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun observeDataChange() {
        viewModel.radiusData.observe(viewLifecycleOwner, Observer {
            fetchRestaurantsData()
        })

        viewModel.currentLocationData.observe(viewLifecycleOwner, Observer {
            fetchRestaurantsData()
        })
    }

    private fun fetchRestaurantsData() {
        lifecycleScope.launchWhenCreated {
            val radius = if (viewModel.radiusData.value == null) DistanceMappingHelper.INITIAL_RADIUS_IN_METERS else viewModel.radiusData.value!!
            if (viewModel.currentLocationData.value != null) {
                viewModel.fetchRestaurantsData(
                    viewModel.currentLocationData.value!!.latitude,
                    viewModel.currentLocationData.value!!.longitude,
                    radius
                ).observe(viewLifecycleOwner, Observer {
                    lifecycleScope.launch {
                        adapter?.submitData(it)
                    }
                })
            }
        }
    }

    private fun checkSettings() {
        if (!LocationHelper.isLocationPermissionGranted(WeakReference(requireContext()))) {
            Snackbar.make(container, getString(R.string.require_permission_message), Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.require_permission_cta)) {
                LocationHelper.requestLocationPermission(WeakReference(this))
            }.show()
        } else {
            checkGpsSettings()
        }
    }

    @SuppressLint("MissingPermission")
    private fun checkGpsSettings() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (!isLocationListenerAdded) {
                Snackbar.make(container, getString(R.string.fetching_location), Snackbar.LENGTH_LONG).show()
                isLocationListenerAdded = true
                val locationRequest = LocationHelper.getLocationRequest()
                val locationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        Snackbar.make(container, getString(R.string.location_received_success), Snackbar.LENGTH_LONG).show()
                        viewModel.setLocationData(locationResult.lastLocation)
                        fusedLocationClient.removeLocationUpdates(this)
                    }
                }
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
            }
        } else {
            Snackbar.make(container, getString(R.string.require_gps_message), Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.require_gps_cta)) {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }.show();
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
            when (requestCode) {
                LocationHelper.PERMISSIONS_REQUEST_LOCATION -> {
                    checkGpsSettings()
                }
            }
        }
    }
}