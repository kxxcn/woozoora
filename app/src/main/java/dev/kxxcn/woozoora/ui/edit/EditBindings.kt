package dev.kxxcn.woozoora.ui.edit

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("app:editItems")
fun setEditItems(view: RecyclerView, items: List<Int>?) {
    items?.let { (view.adapter as? EditAdapter)?.submitList(it) }
}
