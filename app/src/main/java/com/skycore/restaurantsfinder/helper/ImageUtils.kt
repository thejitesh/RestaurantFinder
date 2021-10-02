package com.skycore.restaurantsfinder.helper

import android.content.Context
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.skycore.restaurantsfinder.R
import java.lang.ref.WeakReference

object ImageUtils {

    fun loadImage(
        contextWeakReference: WeakReference<Context>,
        url: String,
        targetView: ImageView,
        @DrawableRes placeHolderImgRes: Int = R.drawable.placeholder_restaurant) {
        val context = contextWeakReference.get()
        if (context != null) {
            Glide.with(context)
                .load(url)
                .placeholder(placeHolderImgRes)
                .into(targetView)
        }
    }
}