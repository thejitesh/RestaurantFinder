package com.skycore.restaurantsfinder.helper

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.lang.ref.WeakReference

object LocationPermissionHelper {

    val MY_PERMISSIONS_REQUEST_LOCATION = 10001

     fun requestLocationPermission(activity : WeakReference<Activity>) {
        ActivityCompat.requestPermissions(activity.get()!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSIONS_REQUEST_LOCATION)
    }

    fun isLocationPermissionGranted(context: WeakReference<Context>) = ContextCompat.checkSelfPermission(context.get()!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

}