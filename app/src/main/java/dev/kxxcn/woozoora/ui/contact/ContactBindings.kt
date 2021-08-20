package dev.kxxcn.woozoora.ui.contact

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.woozoora.ui.invite.item.ContactItem

@BindingAdapter("app:contacts")
fun setInviteList(view: RecyclerView, contacts: List<ContactItem>?) {
    contacts?.let { (view.adapter as? ContactAdapter)?.submitList(it) { view.scheduleLayoutAnimation() } }
}
