package com.skycore.restaurantsfinder.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.skycore.restaurantsfinder.R
import com.skycore.restaurantsfinder.data.model.Businesses

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

        var tvRestaurantName: TextView = view.findViewById(R.id.item_restaurant_list_name)

        fun bind(item: Businesses?) {
            tvRestaurantName.text = item?.name ?: "EMPTY"
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