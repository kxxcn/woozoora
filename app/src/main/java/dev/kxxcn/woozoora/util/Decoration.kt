package dev.kxxcn.woozoora.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.woozoora.common.extension.dpToPx

class HorizontalSpacingDecoration(
    private val size: Int = 10.dpToPx
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildLayoutPosition(view)

        val marginRight = if (position == state.itemCount - 1) {
            size
        } else {
            0
        }

        outRect.left = size
        outRect.right = marginRight
    }
}

class VerticalSpacingDecoration(
    private val size: Int = 30.dpToPx
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildLayoutPosition(view)

        val marginTop = if (position == 0) {
            0
        } else {
            size
        }

        val marginBottom = if (position == state.itemCount - 1) {
            size
        } else {
            0
        }

        outRect.top = marginTop
        outRect.bottom = marginBottom
    }
}
