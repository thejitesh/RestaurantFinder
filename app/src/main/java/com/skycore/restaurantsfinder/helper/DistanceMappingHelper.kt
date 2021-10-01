package com.skycore.restaurantsfinder.helper

object DistanceMappingHelper {

    private const val START_DISTANCE_IN_METERS = 100
    private const val END_DISTANCE_IN_METERS = 5000
    private const val UNIT_DISTANCE = (END_DISTANCE_IN_METERS - START_DISTANCE_IN_METERS) / 100

    fun mapProgressBarDistance(progress: Int) = (progress * UNIT_DISTANCE) + START_DISTANCE_IN_METERS

    fun mapMetersToKilometer(distanceInMeters: Int) = distanceInMeters / 1000
}