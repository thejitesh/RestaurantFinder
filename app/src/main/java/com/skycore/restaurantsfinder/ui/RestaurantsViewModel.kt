package com.skycore.restaurantsfinder.ui

import android.location.Location
import androidx.lifecycle.*
import androidx.paging.*
import com.skycore.restaurantsfinder.data.model.Businesses
import com.skycore.restaurantsfinder.data.repository.RestaurantsDataSource
import com.skycore.restaurantsfinder.helper.Constants.RESTAURANTS_PER_PAGE

class RestaurantsViewModel : ViewModel() {

    val radiusData: MutableLiveData<Int> = MutableLiveData()
    val currentLocationData: MutableLiveData<Location> = MutableLiveData()

    fun fetchRestaurantsData(latitude: Double, longitude: Double, radius: Int): LiveData<PagingData<Businesses>> {
        return Pager(config = PagingConfig(pageSize = RESTAURANTS_PER_PAGE, enablePlaceholders = true),
            pagingSourceFactory = {
                RestaurantsDataSource(latitude, longitude, radius)
            }).liveData
            .cachedIn(viewModelScope)
    }

    fun setLocationData(currentLocation: Location) {
        currentLocationData.value = currentLocation
    }

    fun setRadiusData(newRadius: Int) {
        radiusData.value = newRadius
    }
}