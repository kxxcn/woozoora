package dev.kxxcn.woozoora.ui.direction.statistic.holder

import androidx.databinding.ViewDataBinding
import dev.kxxcn.woozoora.ui.base.BaseHolder
import dev.kxxcn.woozoora.ui.direction.statistic.StatisticViewModel
import dev.kxxcn.woozoora.ui.direction.statistic.item.StatisticItem

abstract class StatisticBaseHolder(binding: ViewDataBinding) : BaseHolder(binding) {

    abstract fun bind(viewModel: StatisticViewModel, item: StatisticItem)
}
