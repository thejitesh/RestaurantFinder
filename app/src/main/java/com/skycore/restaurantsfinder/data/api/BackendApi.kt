package com.skycore.restaurantsfinder.data.api

import com.skycore.restaurantsfinder.data.model.RestaurantsData
import retrofit2.Response
import retrofit2.http.GET

interface BackendService {

    @GET("/business_search")
    suspend fun getRestaurantsData(): Response<RestaurantsData>
}