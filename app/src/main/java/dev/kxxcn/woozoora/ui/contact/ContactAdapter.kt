package dev.kxxcn.woozoora.ui.contact

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import dev.kxxcn.woozoora.ui.base.BaseAdapter
import dev.kxxcn.woozoora.ui.contact.item.ContactItem

class ContactAdapter(
    private val viewModel: ContactViewModel
) : BaseAdapter<ContactItem, ContactHolder>(InviteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        return ContactHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        holder.bind(viewModel, getItem(position))
    }

    class InviteDiffCallback : DiffUtil.ItemCallback<ContactItem>() {

        override fun areItemsTheSame(oldItem: ContactItem, newItem: ContactItem): Boolean {
            return oldItem.phone.hashCode() == newItem.phone.hashCode()
        }

        override fun areContentsTheSame(oldItem: ContactItem, newItem: ContactItem): Boolean {
            return oldItem == newItem
        }
    }
}
