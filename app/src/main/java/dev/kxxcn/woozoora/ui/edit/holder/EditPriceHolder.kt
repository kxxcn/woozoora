package dev.kxxcn.woozoora.ui.edit.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.woozoora.databinding.EditPriceItemBinding
import dev.kxxcn.woozoora.ui.edit.EditViewModel

class EditPriceHolder(
    private val binding: EditPriceItemBinding,
) : EditBaseHolder(binding) {

    override fun bind(viewModel: EditViewModel) {
        with(binding) {
            this.lifecycleOwner = this@EditPriceHolder
            this.viewModel = viewModel
            this.executePendingBindings()
        }
    }

    companion object {

        fun from(parent: ViewGroup): EditPriceHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = EditPriceItemBinding.inflate(inflater, parent, false)
            return EditPriceHolder(binding)
        }
    }
}
