package dev.kxxcn.woozoora.ui.direction.statistic.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.woozoora.databinding.StatisticCategoryItemBinding
import dev.kxxcn.woozoora.ui.direction.statistic.StatisticViewModel
import dev.kxxcn.woozoora.ui.direction.statistic.category.ChildCategoryAdapter
import dev.kxxcn.woozoora.ui.direction.statistic.category.ChildCategorySpacingDecoration
import dev.kxxcn.woozoora.ui.direction.statistic.item.StatisticItem
import dev.kxxcn.woozoora.util.HorizontalLinearLayoutManager

class StatisticCategoryHolder(
    private val binding: StatisticCategoryItemBinding
) : StatisticBaseHolder(binding) {

    override fun bind(viewModel: StatisticViewModel, item: StatisticItem) {
        setupBinding(viewModel, item)
        setupListAdapter(viewModel)
    }

    private fun setupBinding(viewModel: StatisticViewModel, item: StatisticItem) {
        with(binding) {
            this.lifecycleOwner = this@StatisticCategoryHolder
            this.viewModel = viewModel
            this.item = item
            this.executePendingBindings()
        }
    }

    private fun setupListAdapter(viewModel: StatisticViewModel) {
        with(binding.categoryList) {
            if (adapter == null) {
                itemAnimator = null
                layoutManager = HorizontalLinearLayoutManager(context)
                addItemDecoration(ChildCategorySpacingDecoration())
                adapter = ChildCategoryAdapter(viewModel)
            }
        }
    }

    companion object {

        fun from(parent: ViewGroup): StatisticCategoryHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = StatisticCategoryItemBinding.inflate(inflater, parent, false)
            return StatisticCategoryHolder(binding)
        }
    }
}
