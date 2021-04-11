package dev.kxxcn.woozoora.ui.timeline

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.woozoora.common.extension.dpToPx
import dev.kxxcn.woozoora.ui.timeline.holder.TimelineTotalHolder

class TimelineSpacingDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildLayoutPosition(view)
        val marginTop = when (parent.findViewHolderForLayoutPosition(position)) {
            is TimelineTotalHolder -> 20.dpToPx
            else -> 0.dpToPx
        }
        val marginBottom = when (position) {
            state.itemCount - 1 -> 20.dpToPx
            else -> 0.dpToPx
        }

        outRect.top = marginTop
        outRect.bottom = marginBottom
    }
}
