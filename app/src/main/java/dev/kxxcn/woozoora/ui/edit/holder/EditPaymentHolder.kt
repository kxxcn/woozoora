package dev.kxxcn.woozoora.ui.edit.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.woozoora.databinding.EditPaymentItemBinding
import dev.kxxcn.woozoora.ui.edit.EditViewModel

class EditPaymentHolder(
    val binding: EditPaymentItemBinding,
) : EditBaseHolder(binding) {

    override fun bind(viewModel: EditViewModel) {
        with(binding) {
            this.lifecycleOwner = this@EditPaymentHolder
            this.viewModel = viewModel
            this.executePendingBindings()
        }
    }

    companion object {

        fun from(parent: ViewGroup): EditPaymentHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = EditPaymentItemBinding.inflate(inflater, parent, false)
            return EditPaymentHolder(binding)
        }
    }
}
