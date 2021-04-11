package dev.kxxcn.woozoora.ui.timeline

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.woozoora.common.extension.dpToPx
import dev.kxxcn.woozoora.domain.model.TransactionData

@BindingAdapter("app:timeline")
fun setTimelineList(view: RecyclerView, items: List<TransactionData>?) {
    items?.let {
        val newList = TimelineAdapter.create(it)
        (view.adapter as? TimelineAdapter)?.submitList(newList) { view.scheduleLayoutAnimation() }
    }
}

@BindingAdapter("app:marginTop")
fun setMarginTop(view: View, isHeader: Boolean) {
    val marginTop = if (isHeader) 10.dpToPx else 0
    view.updateLayoutParams<ConstraintLayout.LayoutParams> { setMargins(0, marginTop, 0, 0) }
}
