package com.skycore.restaurantsfinder.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.skycore.restaurantsfinder.R
import com.skycore.restaurantsfinder.data.model.Businesses
import com.skycore.restaurantsfinder.helper.DistanceMappingHelper
import com.skycore.restaurantsfinder.helper.ImageUtils
import java.lang.ref.WeakReference

class RestaurantsListAdapter : PagingDataAdapter<Businesses, RecyclerView.ViewHolder>(REPO_COMPARATOR) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? RestaurantsListViewHolder)?.bind(item = getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RestaurantsListViewHolder.getInstance(parent)
    }

    class RestaurantsListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        companion object {
            fun getInstance(parent: ViewGroup): RestaurantsListViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(R.layout.item_restaurant_list, parent, false)
                return RestaurantsListViewHolder(view)
            }
        }

        private var imgRestaurantImage: ImageView = view.findViewById(R.id.item_restaurant_image)
        private var tvRestaurantName: TextView = view.findViewById(R.id.item_restaurant_list_name)
        private var tvRestaurantAddress: TextView = view.findViewById(R.id.item_restaurant_address)
        private var tvRestaurantStatus: TextView = view.findViewById(R.id.item_restaurant_status)
        private var tvRestaurantRatting: TextView = view.findViewById(R.id.item_restaurant_ratting)

        fun bind(item: Businesses?) {
            if (item?.image_url != null) {
                ImageUtils.loadImage(
                    WeakReference(imgRestaurantImage.context),
                    item.image_url,
                    imgRestaurantImage,
                    R.drawable.placeholder_restaurant
                )
            }
            tvRestaurantName.text = item?.name ?: "EMPTY"
            var formattedDistance = ""
            if (item?.distance != null) {
                formattedDistance = DistanceMappingHelper.formatDisplayDistance(item.distance, WeakReference(tvRestaurantAddress.context))
            }

            tvRestaurantAddress.text = formattedDistance.plus(item?.location?.display_address?.joinToString(",", prefix = ", "))
            tvRestaurantStatus.text = if (item?.is_closed == true) {
                "Currently CLOSED"
            } else {
                "Currently OPEN"
            }
            tvRestaurantRatting.text = item?.rating?.toString()
        }
    }


    companion object {

        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Businesses>() {
            override fun areItemsTheSame(oldItem: Businesses, newItem: Businesses): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Businesses, newItem: Businesses): Boolean =
                oldItem == newItem
        }
    }
}