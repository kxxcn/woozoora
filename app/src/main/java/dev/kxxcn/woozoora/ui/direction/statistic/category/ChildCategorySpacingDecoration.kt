package dev.kxxcn.woozoora.ui.direction.statistic.category

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.woozoora.common.extension.dpToPx

class ChildCategorySpacingDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val marginRight = when (parent.getChildLayoutPosition(view)) {
            state.itemCount - 1 -> 0.dpToPx
            else -> 10.dpToPx
        }
        outRect.right = marginRight
    }
}
