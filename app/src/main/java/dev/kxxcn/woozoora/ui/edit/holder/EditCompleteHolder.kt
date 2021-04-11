package dev.kxxcn.woozoora.ui.edit.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.woozoora.databinding.EditCompleteItemBinding
import dev.kxxcn.woozoora.ui.edit.EditViewModel

class EditCompleteHolder(
    private val binding: EditCompleteItemBinding,
) : EditBaseHolder(binding) {

    override fun bind(viewModel: EditViewModel) {
        with(binding) {
            this.lifecycleOwner = this@EditCompleteHolder
            this.viewModel = viewModel
            this.executePendingBindings()
        }
    }

    companion object {

        fun from(parent: ViewGroup): EditCompleteHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = EditCompleteItemBinding.inflate(inflater, parent, false)
            return EditCompleteHolder(binding)
        }
    }
}
