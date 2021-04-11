package dev.kxxcn.woozoora.ui.direction.statistic.category

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.woozoora.databinding.ChildCategoryItemBinding
import dev.kxxcn.woozoora.ui.base.BaseHolder
import dev.kxxcn.woozoora.ui.direction.statistic.StatisticViewModel
import dev.kxxcn.woozoora.ui.direction.statistic.item.CategoryItem

class ChildCategoryHolder(
    private val binding: ChildCategoryItemBinding,
) : BaseHolder(binding) {

    fun bind(viewModel: StatisticViewModel, item: CategoryItem) {
        with(binding) {
            this.lifecycleOwner = this@ChildCategoryHolder
            this.viewModel = viewModel
            this.item = item
            this.rank = adapterPosition + 1
            this.executePendingBindings()
        }
    }

    companion object {

        fun from(parent: ViewGroup): ChildCategoryHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ChildCategoryItemBinding.inflate(inflater, parent, false)
            return ChildCategoryHolder(binding)
        }
    }
}
