package dev.kxxcn.woozoora.ui.notification.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.woozoora.databinding.NotificationChartItemBinding
import dev.kxxcn.woozoora.domain.model.StatisticData
import dev.kxxcn.woozoora.ui.base.BaseHolder
import dev.kxxcn.woozoora.ui.notification.NotificationViewModel

class NotificationChartHolder(
    private val binding: NotificationChartItemBinding
) : BaseHolder(binding) {

    fun bind(viewModel: NotificationViewModel, statistic: StatisticData) {
        with(binding) {
            this.lifecycleOwner = this@NotificationChartHolder
            this.viewModel = viewModel
            this.statistic = statistic
            this.executePendingBindings()
        }
    }

    companion object {

        fun from(parent: ViewGroup): NotificationChartHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = NotificationChartItemBinding.inflate(inflater, parent, false)
            return NotificationChartHolder(binding)
        }
    }
}