package dev.kxxcn.woozoora.ui.direction.history

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.woozoora.common.extension.dpToPx

class HistorySpacingDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildLayoutPosition(view)

        val marginBottom = if (position == state.itemCount - 1) {
            80.dpToPx
        } else {
            0
        }

        outRect.bottom = marginBottom
        outRect.top = 10.dpToPx
    }
}
