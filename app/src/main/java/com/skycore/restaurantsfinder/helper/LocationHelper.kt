package com.skycore.restaurantsfinder.helper

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.LocationRequest
import java.lang.ref.WeakReference

object LocationHelper {

    const val PERMISSIONS_REQUEST_LOCATION = 10001

    fun requestLocationPermission(contextFragment: WeakReference<Fragment>) {
        contextFragment.get()?.requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_REQUEST_LOCATION)
    }

    fun isLocationPermissionGranted(context: WeakReference<Context>) = ContextCompat.checkSelfPermission(context.get()!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    fun getLocationRequest() = LocationRequest.create().apply {
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        interval = 1000
        fastestInterval = 500
    }


}