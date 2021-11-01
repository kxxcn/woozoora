package dev.kxxcn.woozoora.common.extension

import android.app.Activity
import android.os.Build
import android.util.DisplayMetrics

val Activity.displayMetrics: DisplayMetrics
    get() = DisplayMetrics().also {
        if (Build.VERSION.SDK_INT >= 30) {
            display?.apply { getRealMetrics(it) }
        } else {
            windowManager.defaultDisplay.getMetrics(it)
        }
    }
