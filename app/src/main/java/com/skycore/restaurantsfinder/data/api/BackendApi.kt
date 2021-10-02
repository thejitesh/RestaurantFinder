package com.skycore.restaurantsfinder.data.api

import com.skycore.restaurantsfinder.data.model.RestaurantsData
import com.skycore.restaurantsfinder.helper.Constants.DEFAULT_SORT_ORDER_FOR_RESTAURANT_LIST
import com.skycore.restaurantsfinder.helper.Constants.RESTAURANTS_PER_PAGE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface BackendApi {

    @Headers("Content-Type: application/json")
    @GET("search")
    suspend fun getRestaurantsData(
        @Header("Authorization") authHeader: String = "Bearer XPFgzKwZGK1yqRxHi0d5xsARFOLpXIvccQj5jekqTnysweGyoIfVUHcH2tPfGq5Oc9kwKHPkcOjk2d1Xobn7aTjOFeop8x41IUfVvg2Y27KiINjYPADcE7Qza0RkX3Yx",
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("offset") offset: Int,
        @Query("radius") radius: Int = 0,
        @Query("limit") limit: Int = RESTAURANTS_PER_PAGE,
        @Query("sort_by") sortBy: String = DEFAULT_SORT_ORDER_FOR_RESTAURANT_LIST
    ): RestaurantsData
}