package dev.kxxcn.woozoora.ui.direction.statistic

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.woozoora.common.extension.dpToPx
import dev.kxxcn.woozoora.ui.direction.statistic.holder.StatisticCategoryHolder
import dev.kxxcn.woozoora.ui.direction.statistic.holder.StatisticTitleHolder

class StatisticSpacingDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildLayoutPosition(view)
        val holder = parent.findViewHolderForAdapterPosition(position)

        val marginTop = when {
            position == 0 -> 0.dpToPx
            holder is StatisticCategoryHolder -> 20.dpToPx
            holder is StatisticTitleHolder -> 50.dpToPx
            else -> 40.dpToPx
        }

        val marginBottom = when (position) {
            state.itemCount - 1 -> 80.dpToPx
            else -> 0.dpToPx
        }

        outRect.top = marginTop
        outRect.bottom = marginBottom
    }
}
