package com.skycore.restaurantsfinder.ui

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skycore.restaurantsfinder.data.model.RestaurantsData
import com.skycore.restaurantsfinder.data.repository.RestaurantsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RestaurantsViewModel : ViewModel() {

    private val restaurantsRepository = RestaurantsRepository()
    val restaurantsData: MutableLiveData<RestaurantsData> = MutableLiveData()
    val currentLocationData: MutableLiveData<Location> = MutableLiveData()

    fun fetchRestaurantsData(latitude: Double, longitude: Double, radius: Int) {
        viewModelScope.launch {
            restaurantsData.value = restaurantsRepository.getRestaurantsData(latitude, longitude, radius)
        }
    }

    fun setLocationData(currentLocation: Location) {
        currentLocationData.value = currentLocation
    }
}