package dev.kxxcn.woozoora.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import dev.kxxcn.woozoora.common.Payment
import dev.kxxcn.woozoora.common.base.Animatable
import dev.kxxcn.woozoora.common.extension.countAnimation
import dev.kxxcn.woozoora.databinding.PaymentViewBinding
import java.text.NumberFormat

class PaymentView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), LifecycleOwner, Animatable {

    private val lifecycleRegistry by lazy { LifecycleRegistry(this) }

    private val binding: PaymentViewBinding

    private var ratio = 0f

    init {
        val inflater = LayoutInflater.from(context)
        binding = PaymentViewBinding.inflate(inflater, this, true)
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

    override fun playAnimation() {
        binding.ratioText.countAnimation(to = ratio) {
            NumberFormat.getPercentInstance().format(it)
        }
    }

    fun bind(payment: Payment, ratio: Float) {
        with(binding) {
            this.lifecycleOwner = this@PaymentView
            this.payment = payment
            this.executePendingBindings()
        }
        this.ratio = ratio
    }
}
