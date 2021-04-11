package dev.kxxcn.woozoora.ui.direction.statistic.category

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import dev.kxxcn.woozoora.ui.base.BaseAdapter
import dev.kxxcn.woozoora.ui.direction.statistic.StatisticViewModel
import dev.kxxcn.woozoora.ui.direction.statistic.item.CategoryItem

class ChildCategoryAdapter(
    private val viewModel: StatisticViewModel
) : BaseAdapter<CategoryItem, ChildCategoryHolder>(CategoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildCategoryHolder {
        return ChildCategoryHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ChildCategoryHolder, position: Int) {
        holder.bind(viewModel, getItem(position))
    }

    class CategoryDiffCallback : DiffUtil.ItemCallback<CategoryItem>() {

        override fun areItemsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
            return oldItem.transactions == newItem.transactions
        }

        override fun areContentsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
            return oldItem == newItem
        }
    }
}
