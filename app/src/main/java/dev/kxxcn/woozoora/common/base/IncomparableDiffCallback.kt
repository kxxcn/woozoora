package dev.kxxcn.woozoora.common.base

import androidx.recyclerview.widget.DiffUtil

class IncomparableDiffCallback<T> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return false
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return false
    }
}
