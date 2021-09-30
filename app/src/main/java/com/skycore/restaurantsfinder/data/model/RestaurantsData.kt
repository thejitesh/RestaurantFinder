package com.skycore.restaurantsfinder.data

import com.google.gson.annotations.SerializedName

data class RestaurantsData(
    @SerializedName("businesses") val businesses: List<Businesses>,
    @SerializedName("total") val total: Int,
    @SerializedName("region") val region: Region
)