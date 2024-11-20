package com.sanjayprajapat.showmoretextview.utils

import android.content.Context
import android.graphics.Typeface
import androidx.core.content.ContextCompat

fun Context.getColorCompat(colorResId: Int): Int {
    return ContextCompat.getColor(this, colorResId)
}
fun Context.getFontFamilyBold(fontPath: String, style: Int=Typeface.BOLD): Typeface {
    return Typeface.create(fontPath, style)
}


fun Context.getFontFamilyMedium(fontPath: String, style: Int=Typeface.NORMAL): Typeface {
    return Typeface.create(fontPath, style)
}
