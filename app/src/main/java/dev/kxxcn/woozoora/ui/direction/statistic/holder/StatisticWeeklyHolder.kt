package dev.kxxcn.woozoora.ui.direction.statistic.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.woozoora.databinding.StatisticWeeklyItemBinding
import dev.kxxcn.woozoora.ui.direction.statistic.StatisticViewModel
import dev.kxxcn.woozoora.ui.direction.statistic.item.StatisticItem

class StatisticWeeklyHolder(
    private val binding: StatisticWeeklyItemBinding
) : StatisticBaseHolder(binding) {

    override fun bind(viewModel: StatisticViewModel, item: StatisticItem) {
        with(binding) {
            this.lifecycleOwner = this@StatisticWeeklyHolder
            this.viewModel = viewModel
            this.item = item
            this.week = adapterPosition
            this.executePendingBindings()
        }
    }

    companion object {

        fun from(parent: ViewGroup): StatisticWeeklyHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = StatisticWeeklyItemBinding.inflate(inflater, parent, false)
            return StatisticWeeklyHolder(binding)
        }
    }
}
