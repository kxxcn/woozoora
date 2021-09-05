package dev.kxxcn.woozoora.ui.edit

import android.content.res.ColorStateList
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.Category
import dev.kxxcn.woozoora.common.extension.color

@BindingAdapter("app:editItems")
fun setEditItems(view: RecyclerView, items: List<Int>?) {
    items?.let { (view.adapter as? EditAdapter)?.submitList(it) }
}

@BindingAdapter("app:selectedChip")
fun setSelectedChip(view: ChipGroup, category: Category?) {
    view.removeAllViews()
    val context = view.context
    Category.values()
        .filter { it != Category.NONE }
        .forEach {
            val isSelected = it == category
            val backgroundColor = if (isSelected) R.color.white01 else R.color.white02
            val strokeColor = if (isSelected) R.color.primaryPink else R.color.white02
            val textColor = if (isSelected) R.color.primaryPink else R.color.grey04
            Chip(context).apply {
                setText(it.nameRes)
                setTextColor(context.color(textColor))
                chipBackgroundColor = ColorStateList.valueOf(context.color(backgroundColor))
                chipStrokeColor = ColorStateList.valueOf(context.color(strokeColor))
                chipStrokeWidth = 2f
                checkedIcon = null
                isCheckable = true
            }.also { chip ->
                view.addView(chip)
            }
        }
}