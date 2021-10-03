package com.skycore.restaurantsfinder.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.skycore.restaurantsfinder.R
import com.skycore.restaurantsfinder.data.model.Businesses
import com.skycore.restaurantsfinder.helper.DistanceMappingHelper
import com.skycore.restaurantsfinder.helper.ImageUtils
import com.skycore.restaurantsfinder.helper.TextUtils
import java.lang.ref.WeakReference

class RestaurantsListViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    private var imgRestaurantImage: ImageView = view.findViewById(R.id.item_restaurant_image)
    private var tvRestaurantName: TextView = view.findViewById(R.id.item_restaurant_list_name)
    private var tvRestaurantAddress: TextView = view.findViewById(R.id.item_restaurant_address)
    private var tvRestaurantStatus: TextView = view.findViewById(R.id.item_restaurant_status)
    private var tvRestaurantRatting: TextView = view.findViewById(R.id.item_restaurant_ratting)

    fun bind(item: Businesses?) {
        populateImage(item)
        tvRestaurantName.text = item?.name ?: TextUtils.EMPTY
        tvRestaurantAddress.text = getAddressLine(item)
        tvRestaurantStatus.text = getStatusLiteral(item)
        tvRestaurantRatting.text = item?.rating?.toString()
    }

    private fun populateImage(item: Businesses?) {
        if (item?.image_url != null) {
            ImageUtils.loadImage(
                WeakReference(imgRestaurantImage.context),
                item.image_url,
                imgRestaurantImage,
                R.drawable.placeholder_restaurant
            )
        }
    }

    private fun getAddressLine(item: Businesses?): String {
        var formattedDistance = TextUtils.EMPTY
        if (item?.distance != null) {
            formattedDistance = DistanceMappingHelper.formatDisplayDistance(item.distance, WeakReference(tvRestaurantAddress.context))
        }
        return formattedDistance.plus(item?.location?.display_address?.joinToString(TextUtils.COMMA, prefix = TextUtils.COMMA_SPACE_APPEND))
    }

    private fun getStatusLiteral(item: Businesses?) = if (item?.is_closed == true) {
        view.context.getString(R.string.restaurants_status_closed)
    } else {
        view.context.getString(R.string.restaurants_status_open)
    }

    companion object {
        fun getInstance(parent: ViewGroup): RestaurantsListViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.item_restaurant_list, parent, false)
            return RestaurantsListViewHolder(view)
        }
    }
}