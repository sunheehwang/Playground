package com.happy.playground.util.glide

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions

class PlaygroundGlideModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        //val activityManager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val defaultOptions = RequestOptions()
            .format(DecodeFormat.PREFER_RGB_565)
            //.disallowHardwareConfig() // disable hardware bitmaps as they don't play nicely with Palette
        builder.setDefaultRequestOptions(defaultOptions)
    }

    override fun isManifestParsingEnabled(): Boolean = false

}