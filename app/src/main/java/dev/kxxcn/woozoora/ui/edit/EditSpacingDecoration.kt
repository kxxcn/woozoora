package dev.kxxcn.woozoora.ui.edit

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.woozoora.common.extension.dpToPx

class EditSpacingDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildLayoutPosition(view)

        val (marginTop, marginBottom) = when (position) {
            state.itemCount - 1 -> 50.dpToPx to 40.dpToPx
            else -> 15.dpToPx to 0
        }

        outRect.top = marginTop
        outRect.bottom = marginBottom
    }
}
