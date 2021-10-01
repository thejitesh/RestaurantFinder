package com.skycore.restaurantsfinder.data.model

import com.google.gson.annotations.SerializedName

data class Location (
	@SerializedName("address1") val address1 : String,
	@SerializedName("address2") val address2 : String,
	@SerializedName("address3") val address3 : String,
	@SerializedName("city") val city : String,
	@SerializedName("zip_code") val zip_code : Int,
	@SerializedName("country") val country : String,
	@SerializedName("state") val state : String,
	@SerializedName("display_address") val display_address : List<String>
)