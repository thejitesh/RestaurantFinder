package com.skycore.restaurantsfinder.helper

import android.content.Context
import com.skycore.restaurantsfinder.R
import java.lang.ref.WeakReference

object DistanceMappingHelper {

    const val INITIAL_RADIUS_IN_METERS = 100
    private const val FINAL_RADIUS_IN_METERS = 5000
    private const val UNIT_DISTANCE = (FINAL_RADIUS_IN_METERS - INITIAL_RADIUS_IN_METERS) / 100

    fun mapProgressBarDistance(progress: Int) = (progress * UNIT_DISTANCE) + INITIAL_RADIUS_IN_METERS

    fun formatDistance(progress: Int, contextWeakRef: WeakReference<Context>): String {
        val context: Context? = contextWeakRef.get()
        val distanceInMeters = mapProgressBarDistance(progress)
        return if (distanceInMeters < 1000) {
            context?.getString(R.string.distance_in_meters, distanceInMeters) ?: distanceInMeters.toString()
        } else {
            val distanceInKm: Float = distanceInMeters.toFloat() / 1000
            context?.getString(R.string.distance_in_kilo_meters, distanceInKm) ?: distanceInMeters.toString()
        }
    }

    fun formatDisplayDistance(distanceInMeter: Double, contextWeakRef: WeakReference<Context>): String {
        val context: Context? = contextWeakRef.get()
        return if (distanceInMeter < 1000.0) {
            context?.getString(R.string.item_restaurant_lis_distance_in_meters, distanceInMeter) ?: distanceInMeter.toString()
        } else {
            val distanceInKm = distanceInMeter / 1000
            context?.getString(R.string.item_restaurant_lis_distance_in_kilo_meters, distanceInKm) ?: distanceInKm.toString()
        }
    }
}