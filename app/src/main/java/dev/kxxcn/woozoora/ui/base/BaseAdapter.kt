package dev.kxxcn.woozoora.ui.base

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder>(
    callback: DiffUtil.ItemCallback<T>,
) : ListAdapter<T, VH>(callback) {

    override fun onViewAttachedToWindow(holder: VH) {
        super.onViewAttachedToWindow(holder)
        (holder as? BaseHolder)?.onAttach()
    }

    override fun onViewDetachedFromWindow(holder: VH) {
        (holder as? BaseHolder)?.onDetach()
        super.onViewDetachedFromWindow(holder)
    }

    fun isUpdatable(newList: List<T>): Boolean {
        return newList != currentList
    }
}
