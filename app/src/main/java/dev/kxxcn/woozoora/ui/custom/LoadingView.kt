package dev.kxxcn.woozoora.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import dev.kxxcn.woozoora.databinding.LoadingViewBinding

class LoadingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), LifecycleOwner {

    private val lifecycleRegistry by lazy { LifecycleRegistry(this) }

    private val binding: LoadingViewBinding

    var lottieFile: String = "lottie_loading.json"
        set(value) = binding.animationView.setAnimation(value)

    init {
        val inflater = LayoutInflater.from(context)
        binding = LoadingViewBinding.inflate(inflater, this, true)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
    }

    override fun onDetachedFromWindow() {
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
        super.onDetachedFromWindow()
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }
}
