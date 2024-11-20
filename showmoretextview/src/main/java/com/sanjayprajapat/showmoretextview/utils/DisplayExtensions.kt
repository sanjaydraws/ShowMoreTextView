package com.sanjayprajapat.showmoretextview.utils


import android.content.Context

/**
 * Extension function to convert a Float value to sp (scaled pixels) based on the context's density.
 */
fun Float?.toSp(context: Context): Float {
    return this?.times(context.resources.displayMetrics.scaledDensity) ?: 0f
}
