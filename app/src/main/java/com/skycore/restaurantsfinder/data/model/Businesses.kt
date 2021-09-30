package com.skycore.restaurantsfinder.data

import com.google.gson.annotations.SerializedName

data class Businesses(
    @SerializedName("id") val id: String,
    @SerializedName("alias") val alias: String,
    @SerializedName("name") val name: String,
    @SerializedName("image_url") val image_url: String,
    @SerializedName("is_closed") val is_closed: Boolean,
    @SerializedName("url") val url: String,
    @SerializedName("review_count") val review_count: Int,
    @SerializedName("categories") val categories: List<Categories>,
    @SerializedName("rating") val rating: Double,
    @SerializedName("coordinates") val coordinates: Coordinates,
    @SerializedName("transactions") val transactions: List<String>,
    @SerializedName("price") val price: String,
    @SerializedName("location") val location: Location,
    @SerializedName("phone") val phone: Int,
    @SerializedName("display_phone") val display_phone: String,
    @SerializedName("distance") val distance: Double
)