package com.skycore.restaurantsfinder.helper

object DistanceMappingHelper {

    const val INITIAL_RADIUS_IN_METERS = 100
    private const val FINAL_RADIUS_IN_METERS = 5000
    private const val UNIT_DISTANCE = (FINAL_RADIUS_IN_METERS - INITIAL_RADIUS_IN_METERS) / 100

    fun mapProgressBarDistance(progress: Int) = (progress * UNIT_DISTANCE) + INITIAL_RADIUS_IN_METERS

    fun mapMetersToKilometer(distanceInMeters: Int) = distanceInMeters / 1000
}