package dev.kxxcn.woozoora.ui.direction.history

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import dev.kxxcn.woozoora.ui.base.BaseAdapter
import dev.kxxcn.woozoora.ui.direction.history.holder.HistoryDayHolder
import dev.kxxcn.woozoora.ui.direction.history.item.DayItem

class HistoryDayAdapter(
    private val viewModel: HistoryViewModel,
) : BaseAdapter<DayItem, HistoryDayHolder>(DayDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryDayHolder {
        return HistoryDayHolder.from(parent)
    }

    override fun onBindViewHolder(holder: HistoryDayHolder, position: Int) {
        holder.bind(getItem(position), viewModel)
    }

    fun submitListAndScroll(newList: List<DayItem>) {
        submitList(newList) { viewModel.select() }
    }

    class DayDiffCallback : DiffUtil.ItemCallback<DayItem>() {

        override fun areItemsTheSame(oldItem: DayItem, newItem: DayItem): Boolean {
            return oldItem.day == newItem.day
        }

        override fun areContentsTheSame(oldItem: DayItem, newItem: DayItem): Boolean {
            return oldItem == newItem
        }
    }
}
