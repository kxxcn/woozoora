package dev.kxxcn.woozoora.ui.reply

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.woozoora.domain.model.AskData

@BindingAdapter("app:asks")
fun setAskList(view: RecyclerView, items: List<AskData>?) {
    items?.let { (view.adapter as? ReplyAdapter)?.submitList(it) }
}
