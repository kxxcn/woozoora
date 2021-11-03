package dev.kxxcn.woozoora.ui.direction.home

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.woozoora.common.extension.dpToPx
import dev.kxxcn.woozoora.ui.direction.home.holder.HomeBudgetHolder
import dev.kxxcn.woozoora.ui.direction.home.holder.HomeGroupHolder
import dev.kxxcn.woozoora.ui.direction.home.holder.HomeNativeAdsHolder
import dev.kxxcn.woozoora.ui.direction.home.holder.HomeTitleHolder

class HomeSpacingDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildLayoutPosition(view)

        val marginTop = when (parent.findViewHolderForAdapterPosition(position)) {
            is HomeBudgetHolder,
            is HomeGroupHolder,
            is HomeNativeAdsHolder -> 20.dpToPx
            is HomeTitleHolder -> 30.dpToPx
            else -> 10.dpToPx
        }

        val marginBottom = if (position == state.itemCount - 1) {
            80.dpToPx
        } else {
            0
        }

        outRect.top = marginTop
        outRect.bottom = marginBottom
    }
}
