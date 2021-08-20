package dev.kxxcn.woozoora.ui.contact

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.woozoora.databinding.ContactItemBinding
import dev.kxxcn.woozoora.ui.base.BaseHolder
import dev.kxxcn.woozoora.ui.invite.InviteViewModel
import dev.kxxcn.woozoora.ui.invite.item.ContactItem

class ContactHolder(
    private val binding: ContactItemBinding
) : BaseHolder(binding) {

    fun bind(viewModel: InviteViewModel, item: ContactItem) {
        with(binding) {
            this.lifecycleOwner = this@ContactHolder
            this.viewModel = viewModel
            this.item = item
            this.executePendingBindings()
        }
    }

    companion object {

        fun from(parent: ViewGroup): ContactHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ContactItemBinding.inflate(inflater, parent, false)
            return ContactHolder(binding)
        }
    }
}
