package dev.kxxcn.woozoora.ui.direction.more

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.woozoora.common.extension.dpToPx

class MoreSpacingDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val marginBottom = when (parent.getChildLayoutPosition(view)) {
            state.itemCount - 1 -> 40.dpToPx
            else -> 0
        }
        outRect.top = 10.dpToPx
        outRect.bottom = marginBottom
    }
}
