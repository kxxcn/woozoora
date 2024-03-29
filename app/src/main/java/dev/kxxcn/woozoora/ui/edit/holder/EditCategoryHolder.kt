package dev.kxxcn.woozoora.ui.edit.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.children
import com.google.android.material.chip.Chip
import dev.kxxcn.woozoora.databinding.EditCategoryItemBinding
import dev.kxxcn.woozoora.ui.edit.EditViewModel

class EditCategoryHolder(
    private val binding: EditCategoryItemBinding,
) : EditBaseHolder(binding) {

    override fun bind(viewModel: EditViewModel) {
        setupBinding(viewModel)
        setupListener(viewModel)
    }

    private fun setupBinding(viewModel: EditViewModel) {
        with(binding) {
            this.lifecycleOwner = this@EditCategoryHolder
            this.viewModel = viewModel
            this.executePendingBindings()
        }
    }

    private fun setupListener(viewModel: EditViewModel) {
        binding.categoryGroup.setOnCheckedChangeListener { group, checkedId ->
            group.children
                .filter { (it as Chip).isChecked }
                .firstOrNull()
                ?.let { viewModel.category(it.id) }
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
