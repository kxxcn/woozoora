package dev.kxxcn.woozoora.ui.direction.statistic.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.woozoora.databinding.StatisticTitleItemBinding
import dev.kxxcn.woozoora.ui.base.BaseHolder

class StatisticTitleHolder(
    private val binding: StatisticTitleItemBinding
) : BaseHolder(binding) {

    fun bind(titleRes: Int?) {
        with(binding) {
            this.lifecycleOwner = this@StatisticTitleHolder
            this.titleRes = titleRes
            this.executePendingBindings()
        }
    }

    companion object {

        fun from(parent: ViewGroup): StatisticTitleHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = StatisticTitleItemBinding.inflate(inflater, parent, false)
            return StatisticTitleHolder(binding)
        }
    }
}
