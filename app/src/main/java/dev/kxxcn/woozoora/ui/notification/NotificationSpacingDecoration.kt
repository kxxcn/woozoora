package dev.kxxcn.woozoora.ui.notification

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.woozoora.common.extension.dpToPx

class NotificationSpacingDecoration : RecyclerView.ItemDecoration() {

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
        outRect.top = 20.dpToPx
        outRect.bottom = marginBottom
    }
}
