package com.skycore.restaurantsfinder.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.skycore.restaurantsfinder.data.api.BackendApi
import com.skycore.restaurantsfinder.data.api.BackendServiceBuilder
import com.skycore.restaurantsfinder.data.model.Businesses
import com.skycore.restaurantsfinder.helper.Constants.RESTAURANTS_PER_PAGE

class RestaurantsDataSource(
    private val latitude: Double,
    private val longitude: Double,
    private val radius: Int
) : PagingSource<Int, Businesses>() {

    private val backendApi: BackendApi = BackendServiceBuilder.buildBackendApi()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Businesses> {
        return try {
            val currentPageOffset = params.key ?: 0
            //NYC info : latitude = 40.730610, longitude = -73.935242
            //val response = backendApi.getRestaurantsData( latitude = 40.730610, longitude = -73.935242, radius = radius, offset = currentPageOffset)
            val response = backendApi.getRestaurantsData(latitude = latitude, longitude = longitude, radius = radius, offset = currentPageOffset)
            val prevPageOffset = currentPageOffset - RESTAURANTS_PER_PAGE
            val prevKey = if (currentPageOffset == 0 || prevPageOffset <= 0) {
                null
            } else {
                prevPageOffset
            }
            val nextKey = if (response.total - currentPageOffset < RESTAURANTS_PER_PAGE) {
                null
            } else {
                currentPageOffset.plus(RESTAURANTS_PER_PAGE)
            }
            LoadResult.Page(
                data = response.businesses,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Businesses>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}