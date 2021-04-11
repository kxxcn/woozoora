package dev.kxxcn.woozoora.ui.direction.more

import android.animation.ValueAnimator
import androidx.core.animation.addListener
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.DURATION_ONE_SECONDS

@BindingAdapter("app:lottie_toggle")
fun setToggle(view: LottieAnimationView, isOn: Boolean) {
    val key = R.id.tag_toggle_animation_view
    (view.getTag(key) as? ValueAnimator)?.let {
        it.cancel()
        it.removeAllUpdateListeners()
        view.setTag(key, null)
    }

    val currentProgress = view.progress
    if (isOn) {
        ValueAnimator.ofFloat(currentProgress, 0.5f)
    } else {
        ValueAnimator.ofFloat(currentProgress, 0f)
    }.also { animator ->
        animator.apply {
            addUpdateListener { animator ->
                val value = animator.animatedValue as Float
                view.progress = value
            }
            addListener(onEnd = { removeAllUpdateListeners() })
            duration = DURATION_ONE_SECONDS
        }.also {
            view.setTag(key, it)
            it.start()
        }
    }
}
