package com.sanjayprajapat.showmoretextview.utils

import org.jetbrains.annotations.NotNull




@NotNull
fun Any?.safeToInt(): Int {
    return try {
        this?.toString()?.toInt() ?: 0
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        0
    }
}
@NotNull
fun Any?.safeToFloat(): Float {
    return try {
        this?.toString()?.toFloat() ?: 0f
    } catch (e: Exception) {
        e.printStackTrace()
        0f
    }
}
