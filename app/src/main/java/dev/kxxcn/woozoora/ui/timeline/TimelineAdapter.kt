package dev.kxxcn.woozoora.ui.timeline

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.woozoora.common.extension.day
import dev.kxxcn.woozoora.domain.model.TransactionData
import dev.kxxcn.woozoora.ui.base.BaseAdapter
import dev.kxxcn.woozoora.ui.timeline.holder.TimelineBaseHolder
import dev.kxxcn.woozoora.ui.timeline.holder.TimelineTotalHolder
import dev.kxxcn.woozoora.ui.timeline.holder.TimelineTransactionHolder
import dev.kxxcn.woozoora.ui.timeline.item.TimelineItem

class TimelineAdapter(
    private val viewModel: TimelineViewModel,
) : BaseAdapter<TimelineItem, RecyclerView.ViewHolder>(TimelineDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_TOTAL -> TimelineTotalHolder.from(parent)
            else -> TimelineTransactionHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? TimelineBaseHolder)?.bind(viewModel, getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type
    }

    class TimelineDiffCallback : DiffUtil.ItemCallback<TimelineItem>() {

        override fun areItemsTheSame(oldItem: TimelineItem, newItem: TimelineItem): Boolean {
            return oldItem.transaction == newItem.transaction
        }

        override fun areContentsTheSame(oldItem: TimelineItem, newItem: TimelineItem): Boolean {
            return oldItem == newItem
        }
    }

    companion object {

        private const val TYPE_TOTAL = 0

        private const val TYPE_TRANSACTION = 1

        fun create(transactions: List<TransactionData>): List<TimelineItem> {
            return transactions
                .sortedBy { it.date }
                .groupBy { it.date.day }
                .flatMap { (_, filteredList) ->
                    val totalCount = filteredList.size
                    val totalSpending = filteredList.sumBy { it.price }
                    listOf(
                        TimelineItem(
                            TYPE_TOTAL,
                            filteredList.first(),
                            totalCount,
                            totalSpending
                        )
                    ) + filteredList.mapIndexed { index, transaction ->
                        TimelineItem(
                            TYPE_TRANSACTION,
                            transaction,
                            isHeader = index == 0,
                            isDashed = index != filteredList.size - 1
                        )
                    }
                }
        }
    }
}
