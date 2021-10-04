package dev.kxxcn.woozoora.ui.edit

import android.content.res.ColorStateList
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.extension.color
import dev.kxxcn.woozoora.ui.edit.item.EditCategory

@BindingAdapter("app:viewModel")
fun setEditViewModel(view: RecyclerView, viewModel: EditViewModel) {
    val items = if (viewModel.branch == EditBranchType.TRANSACTION) {
        listOf(
            EditAdapter.TYPE_NAME,
            EditAdapter.TYPE_DATE,
            EditAdapter.TYPE_PRICE,
            EditAdapter.TYPE_PAYMENT,
            EditAdapter.TYPE_CATEGORY,
            EditAdapter.TYPE_DESC,
            EditAdapter.TYPE_COMPLETE
        )
    } else {
        listOf(
            EditAdapter.TYPE_NAME,
            EditAdapter.TYPE_DATE,
            EditAdapter.TYPE_PRICE,
            EditAdapter.TYPE_CATEGORY,
            EditAdapter.TYPE_DESC,
            EditAdapter.TYPE_COMPLETE
        )
    }
    items.let { (view.adapter as? EditAdapter)?.submitList(it) }
}

@BindingAdapter("app:chip")
fun setSelectedChip(view: ChipGroup, category: List<EditCategory>?) {
    view.removeAllViews()
    val context = view.context
    category?.forEach {
        val backgroundColor = if (it.isSelected) R.color.white01 else R.color.white02
        val strokeColor = if (it.isSelected) R.color.primaryPink else R.color.white02
        val textColor = if (it.isSelected) R.color.primaryPink else R.color.grey04
        Chip(context).apply {
            setTextColor(context.color(textColor))
            id = it.id
            text = it.name
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
