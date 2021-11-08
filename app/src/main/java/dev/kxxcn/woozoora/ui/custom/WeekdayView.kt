package dev.kxxcn.woozoora.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.databinding.WeekdayViewBinding
import dev.kxxcn.woozoora.util.Converter

class WeekdayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs), LifecycleOwner {

    private val lifecycleRegistry by lazy { LifecycleRegistry(this) }

    private var binding: WeekdayViewBinding

    init {
        R.layout.weekday_view
        val inflater = LayoutInflater.from(context)
        binding = WeekdayViewBinding.inflate(inflater, this, true)
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

    fun bind(weekday: Int) {
        with(binding) {
            this.lifecycleOwner = this@WeekdayView
            this.weekday = Converter.weekdayToResource(weekday.toFloat())
            this.executePendingBindings()
        }
    }
}
