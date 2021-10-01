package com.skycore.restaurantsfinder.data.repository

import com.skycore.restaurantsfinder.data.api.BackendApi
import com.skycore.restaurantsfinder.data.api.BackendServiceBuilder

class RestaurantsRepository {

    private val backendApi: BackendApi = BackendServiceBuilder.buildBackendApi()

    suspend fun getRestaurantsData() = backendApi.getRestaurantsData()
}