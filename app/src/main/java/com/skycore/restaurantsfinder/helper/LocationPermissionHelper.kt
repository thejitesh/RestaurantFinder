package com.skycore.restaurantsfinder.helper

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.lang.ref.WeakReference

object LocationPermissionHelper {

    const val MY_PERMISSIONS_REQUEST_LOCATION = 10001

     fun requestLocationPermission(contextFragment: WeakReference<Fragment>) {
         contextFragment.get()?.requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSIONS_REQUEST_LOCATION)
    }

    fun isLocationPermissionGranted(context: WeakReference<Context>) = ContextCompat.checkSelfPermission(context.get()!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

}