package dev.kxxcn.woozoora.common.extension

import android.content.res.Resources

val Int.dpToPx: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Int.pxToDp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Float.pxToSp: Float
    get() {
        val scale: Float = Resources.getSystem().displayMetrics.scaledDensity
        return this / scale
    }
