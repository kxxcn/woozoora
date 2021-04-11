package dev.kxxcn.woozoora.ui.base

import androidx.databinding.ViewDataBinding
import dev.kxxcn.woozoora.common.base.Animatable

abstract class AnimatableHolder(
    binding: ViewDataBinding
) : BaseHolder(binding), Animatable {

    var isAnimated: Boolean = false

    abstract fun animate()

    override fun playAnimation() {
        if (!isAnimated) {
            animate()
        }
        isAnimated = true
    }

    override fun clearAnimation() {
        isAnimated = false
    }
}
