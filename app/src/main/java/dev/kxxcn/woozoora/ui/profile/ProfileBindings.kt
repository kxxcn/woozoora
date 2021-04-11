package dev.kxxcn.woozoora.ui.profile

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.woozoora.domain.model.UserData

@BindingAdapter("app:profile")
fun setProfileList(view: RecyclerView, user: UserData?) {
    user?.let {
        val newList = ProfileAdapter.create(it)
        (view.adapter as? ProfileAdapter)?.submitList(newList)
    }
}
