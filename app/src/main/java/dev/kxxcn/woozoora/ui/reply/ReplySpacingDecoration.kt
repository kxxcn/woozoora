package dev.kxxcn.woozoora.ui.reply

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.woozoora.common.extension.dpToPx

class ReplySpacingDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val marginBottom = when (parent.getChildLayoutPosition(view)) {
            state.itemCount - 1 -> 20.dpToPx
            else -> 0
        }
        outRect.top = 30.dpToPx
        outRect.bottom = marginBottom
    }
}
