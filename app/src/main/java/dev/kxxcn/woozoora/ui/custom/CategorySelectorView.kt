package dev.kxxcn.woozoora.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import dev.kxxcn.woozoora.common.Category
import dev.kxxcn.woozoora.databinding.CategorySelectorViewBinding
import dev.kxxcn.woozoora.ui.edit.EditViewModel

class CategorySelectorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), LifecycleOwner {

    private val lifecycleRegistry by lazy { LifecycleRegistry(this) }

    private val binding: CategorySelectorViewBinding

    init {
        val inflater = LayoutInflater.from(context)
        binding = CategorySelectorViewBinding.inflate(inflater, this, true)
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

    fun bind(category: Category, viewModel: EditViewModel) {
        with(binding) {
            this.lifecycleOwner = this@CategorySelectorView
            this.category = category
            this.viewModel = viewModel
            this.executePendingBindings()
        }
    }

    fun isSelectedCategory(): Boolean {
        return binding.viewModel?.categoryFilterType?.value == binding.category
    }
}
