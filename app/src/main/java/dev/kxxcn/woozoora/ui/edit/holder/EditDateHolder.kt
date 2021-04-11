package dev.kxxcn.woozoora.ui.edit.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.woozoora.databinding.EditDateItemBinding
import dev.kxxcn.woozoora.ui.edit.EditViewModel

class EditDateHolder(
    private val binding: EditDateItemBinding,
) : EditBaseHolder(binding) {

    override fun bind(viewModel: EditViewModel) {
        with(binding) {
            this.lifecycleOwner = this@EditDateHolder
            this.viewModel = viewModel
            this.executePendingBindings()
        }
    }

    companion object {

        fun from(parent: ViewGroup): EditDateHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = EditDateItemBinding.inflate(inflater, parent, false)
            return EditDateHolder(binding)
        }
    }
}
