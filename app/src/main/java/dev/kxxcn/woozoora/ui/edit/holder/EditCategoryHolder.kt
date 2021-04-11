package dev.kxxcn.woozoora.ui.edit.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.doOnLayout
import dev.kxxcn.woozoora.common.Category
import dev.kxxcn.woozoora.common.extension.dpToPx
import dev.kxxcn.woozoora.databinding.EditCategoryItemBinding
import dev.kxxcn.woozoora.ui.custom.CategorySelectorView
import dev.kxxcn.woozoora.ui.edit.EditViewModel

class EditCategoryHolder(
    private val binding: EditCategoryItemBinding,
) : EditBaseHolder(binding) {

    override fun bind(viewModel: EditViewModel) {
        setupBinding(viewModel)
        setupCategories(viewModel)
    }

    private fun setupBinding(viewModel: EditViewModel) {
        with(binding) {
            this.lifecycleOwner = this@EditCategoryHolder
            this.viewModel = viewModel
            this.executePendingBindings()
        }
    }

    private fun setupCategories(viewModel: EditViewModel) {
        val categories = Category.values()
        categories
            .mapIndexed { index, category ->
                CategorySelectorView(context)
                    .apply { bind(category, viewModel) }
                    .apply {
                        val marginEnd = if (index == categories.size - 1) 0 else 5.dpToPx
                        layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        ).apply { setMargins(0, 0, marginEnd, 0) }
                    }
            }
            .forEach {
                it.doOnLayout { view ->
                    val location = IntArray(2)
                    view.getLocationOnScreen(location)
                    if (it.isSelectedCategory()) {
                        binding.categoryEditScroll.scrollBy(location[0] - 20.dpToPx, 0)
                    }
                }
                binding.categoryEditParent.addView(it)
            }
    }

    companion object {

        fun from(parent: ViewGroup): EditCategoryHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = EditCategoryItemBinding.inflate(inflater, parent, false)
            return EditCategoryHolder(binding)
        }
    }
}
