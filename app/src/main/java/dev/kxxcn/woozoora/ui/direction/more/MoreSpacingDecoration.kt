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
        state: RecyclerView.State,
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildLayoutPosition(view)
        outRect.left = if (position == 0) 0 else 20.dpToPx
        outRect.right = if (position == 0) 0 else 20.dpToPx
        outRect.top = if (position == 1) 20.dpToPx else 10.dpToPx
        outRect.bottom = if (position == state.itemCount - 1) 40.dpToPx else 0
    }
}
