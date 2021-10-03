package com.skycore.restaurantsfinder.ui

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.skycore.restaurantsfinder.data.model.Businesses

class RestaurantsListAdapter : PagingDataAdapter<Businesses, RecyclerView.ViewHolder>(REPO_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RestaurantsListViewHolder.getInstance(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as RestaurantsListViewHolder).bind(getItem(position))
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