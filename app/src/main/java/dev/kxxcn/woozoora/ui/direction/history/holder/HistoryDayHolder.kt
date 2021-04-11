package dev.kxxcn.woozoora.ui.direction.history.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.woozoora.databinding.HistoryDayItemBinding
import dev.kxxcn.woozoora.ui.base.BaseHolder
import dev.kxxcn.woozoora.ui.direction.history.HistoryViewModel
import dev.kxxcn.woozoora.ui.direction.history.item.DayItem

class HistoryDayHolder(
    private val binding: HistoryDayItemBinding
) : BaseHolder(binding) {

    fun bind(item: DayItem, viewModel: HistoryViewModel) {
        with(binding) {
            this.lifecycleOwner = this@HistoryDayHolder
            this.item = item
            this.viewModel = viewModel
            this.executePendingBindings()
        }
    }

    companion object {

        fun from(parent: ViewGroup): HistoryDayHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = HistoryDayItemBinding.inflate(inflater, parent, false)
            return HistoryDayHolder(binding)
        }
    }
}
