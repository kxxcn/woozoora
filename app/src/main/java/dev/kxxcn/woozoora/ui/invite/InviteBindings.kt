package dev.kxxcn.woozoora.ui.invite

import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView

@BindingAdapter("app:lottie_check")
fun setLottieCheck(view: LottieAnimationView, isAgree: Boolean) {
    if (isAgree) {
        view.playAnimation()
    } else {
        if (view.isAnimating) {
            view.cancelAnimation()
        }
        view.progress = 0f
    }
}
