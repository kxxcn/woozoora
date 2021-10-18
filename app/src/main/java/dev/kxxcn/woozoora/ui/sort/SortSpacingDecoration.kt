package dev.kxxcn.woozoora.ui.sort

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.woozoora.common.extension.dpToPx

class SortSpacingDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val marginBottom = when (parent.getChildLayoutPosition(view)) {
            state.itemCount - 1 -> 20.dpToPx
            else -> 0.dpToPx
        }
        outRect.left = 20.dpToPx
        outRect.right = 20.dpToPx
        outRect.top = 10.dpToPx
        outRect.bottom = marginBottom
    }
}
