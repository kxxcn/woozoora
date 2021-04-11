package dev.kxxcn.woozoora.ui.receipt

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.domain.model.HistoryData

@BindingAdapter("app:lottie_play")
fun setLottieAnimation(view: LottieAnimationView, isPlay: Boolean) {
    if (isPlay) {
        view.playAnimation()
    }
}

@BindingAdapter("app:completeText")
fun setCompleteText(view: TextView, history: HistoryData) {
    val context = view.context
    if (history.isNew) {
        context.getString(R.string.complete_registration)
    } else {
        context.getString(R.string.format_user, history.host?.name)
    }.also {
        view.text = it
    }
}
