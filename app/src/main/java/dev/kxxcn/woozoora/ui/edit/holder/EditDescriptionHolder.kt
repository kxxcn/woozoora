package dev.kxxcn.woozoora.ui.edit.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.woozoora.databinding.EditDescriptionItemBinding
import dev.kxxcn.woozoora.ui.edit.EditViewModel

class EditDescriptionHolder(
    private val binding: EditDescriptionItemBinding,
) : EditBaseHolder(binding) {

    override fun bind(viewModel: EditViewModel) {
        with(binding) {
            this.lifecycleOwner = this@EditDescriptionHolder
            this.viewModel = viewModel
            this.executePendingBindings()
        }
    }

    companion object {

        fun from(parent: ViewGroup): EditDescriptionHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = EditDescriptionItemBinding.inflate(inflater, parent, false)
            return EditDescriptionHolder(binding)
        }
    }
}
