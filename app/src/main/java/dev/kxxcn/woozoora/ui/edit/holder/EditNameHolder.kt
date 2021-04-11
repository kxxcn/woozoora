package dev.kxxcn.woozoora.ui.edit.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.woozoora.databinding.EditNameItemBinding
import dev.kxxcn.woozoora.ui.edit.EditViewModel

class EditNameHolder(
    private val binding: EditNameItemBinding,
) : EditBaseHolder(binding) {

    override fun bind(viewModel: EditViewModel) {
        with(binding) {
            this.lifecycleOwner = this@EditNameHolder
            this.viewModel = viewModel
            this.executePendingBindings()
        }
    }

    companion object {

        fun from(parent: ViewGroup): EditNameHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = EditNameItemBinding.inflate(inflater, parent, false)
            return EditNameHolder(binding)
        }
    }
}
