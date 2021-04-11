package dev.kxxcn.woozoora.ui.direction.history.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.woozoora.databinding.HistoryEmptyItemBinding
import dev.kxxcn.woozoora.ui.base.BaseHolder

class HistoryEmptyHolder(
    private val binding: HistoryEmptyItemBinding
) : BaseHolder(binding) {

    fun bind() {
        with(binding) {
            this.lifecycleOwner = this@HistoryEmptyHolder
            this.executePendingBindings()
        }
    }

    companion object {

        fun from(parent: ViewGroup): HistoryEmptyHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = HistoryEmptyItemBinding.inflate(inflater, parent, false)
            return HistoryEmptyHolder(binding)
        }
    }
}
