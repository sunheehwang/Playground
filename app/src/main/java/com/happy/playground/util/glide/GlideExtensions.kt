package com.happy.playground.util.glide

import android.widget.ImageView
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions


fun ImageView?.loadCircleImage(defaultResId: Int, url: String?){
    this?.let {
        GlideApp.with(this).load(url)
            .apply(RequestOptions.circleCropTransform())
            .placeholder(defaultResId)
            .override(this.width / 2, this.height / 2)
            .into(this)
    }
}

fun ImageView?.loadImage(defaultResId: Int, url: String?){
    this?.let {
        GlideApp.with(this).load(url)
            .placeholder(defaultResId)
            .override(this.width, this.height)
            .into(this)
    }
}

fun ImageView?.loadImage(resId: Int){
    this?.let {
        GlideApp.with(this).load(resId)
            .override(this.width, this.height)
            .into(this)
    }
}

fun ImageView?.loadImage(defaultResId: Int, url: String?, radius: Int){
    this?.let {
        GlideApp.with(this).load(url)
            .apply(RequestOptions().transforms(CenterCrop(), RoundedCorners(radius)))
            .placeholder(defaultResId)
            .override(this.width, this.height)
            .into(this)
    }
}

fun ImageView?.loadImage(url: String?, ratio: Float = 1f){
    this?.let {
        GlideApp.with(this).load(url)
            .override((this.width * ratio).toInt(), (this.height * ratio).toInt())
            .into(this)
    }
}
