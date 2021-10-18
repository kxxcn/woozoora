package dev.kxxcn.woozoora.ui.sort

import android.widget.TextView
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.extension.color
import dev.kxxcn.woozoora.ui.sort.item.SortItem

@BindingAdapter("app:category")
fun setSortList(view: RecyclerView, items: List<SortItem>?) {
    items?.let { (view.adapter as? SortAdapter)?.submitList(it) }
}

@BindingAdapter("app:count")
fun setDeletedCountText(view: TextView, text: String) {
    val count = text.replace(("[^\\d.]").toRegex(), "").toInt()
    view.isVisible = count != 0
    val blueColor = view.context.color(R.color.primaryBlue)
    val redColor = view.context.color(R.color.red01)
    val index = text.lastIndexOf(" ")
    val desc = text.substring(0, index)
    val delete = text.substring(index, text.length)
    view.text = buildSpannedString {
        color(blueColor) { append(desc) }
        color(redColor) { append(delete) }
    }
}
