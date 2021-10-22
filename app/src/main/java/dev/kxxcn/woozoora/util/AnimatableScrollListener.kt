package dev.kxxcn.woozoora.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.woozoora.common.base.Animatable

class AnimatableScrollListener : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val manager = recyclerView.layoutManager as? LinearLayoutManager ?: return
        val position = manager.findLastVisibleItemPosition()
        val holder = recyclerView.findViewHolderForAdapterPosition(position)
        (holder as? Animatable)?.playAnimation()
    }
}
