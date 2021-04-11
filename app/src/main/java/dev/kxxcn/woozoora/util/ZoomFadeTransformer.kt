package dev.kxxcn.woozoora.util

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs
import kotlin.math.max

private const val MIN_SCALE = 0.8f
private const val MIN_ALPHA = 0.45f

class ZoomFadeTransformer : ViewPager2.PageTransformer {

    override fun transformPage(page: View, position: Float) {
        when {
            position < -1 -> {
                page.scaleX = MIN_SCALE
                page.scaleY = MIN_SCALE
                page.alpha = MIN_ALPHA
            }
            position <= 1 -> {
                val scaleFactor = max(MIN_SCALE, 1 - abs(position))
                page.scaleX = scaleFactor
                page.scaleY = scaleFactor
                val alphaFactor = max(MIN_ALPHA, 1 - abs(position))
                page.alpha = alphaFactor
            }
            else -> {
                page.scaleX = MIN_SCALE
                page.scaleY = MIN_SCALE
                page.alpha = MIN_ALPHA
            }
        }
    }
}
