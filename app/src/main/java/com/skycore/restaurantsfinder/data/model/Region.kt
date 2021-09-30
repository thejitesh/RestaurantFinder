package com.skycore.restaurantsfinder.data

import com.google.gson.annotations.SerializedName
import com.skycore.restaurantsfinder.data.Center

data class Region (
	@SerializedName("center") val center : Center
)