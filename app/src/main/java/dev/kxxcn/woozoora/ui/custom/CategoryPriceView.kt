package dev.kxxcn.woozoora.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import dev.kxxcn.woozoora.common.base.Animatable
import dev.kxxcn.woozoora.common.extension.countAnimation
import dev.kxxcn.woozoora.databinding.CategoryPriceViewBinding
import dev.kxxcn.woozoora.util.Converter

class CategoryPriceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), LifecycleOwner, Animatable {

    private val lifecycleRegistry by lazy { LifecycleRegistry(this) }

    private val binding: CategoryPriceViewBinding

    private var price = 0

    init {
        val inflater = LayoutInflater.from(context)
        binding = CategoryPriceViewBinding.inflate(inflater, this, true)
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
        binding.moneyText.countAnimation(to = price) {
            Converter.moneyFormat(it.toString())
        }
    }

    fun bind(nameRes: Int, colorRes: Int, price: Int) {
        with(binding) {
            this.lifecycleOwner = this@CategoryPriceView
            this.nameRes = nameRes
            this.colorRes = colorRes
            this.executePendingBindings()
        }
        this.price = price
    }
}
