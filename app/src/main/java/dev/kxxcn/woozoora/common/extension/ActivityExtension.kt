package dev.kxxcn.woozoora.common.extension

import android.app.Activity
import android.util.DisplayMetrics

fun Activity.getDisplaySize(): DisplayMetrics {
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics
}
