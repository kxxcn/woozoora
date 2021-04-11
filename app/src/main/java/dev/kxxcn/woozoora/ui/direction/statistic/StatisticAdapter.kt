package dev.kxxcn.woozoora.ui.direction.statistic

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.domain.model.OverviewData
import dev.kxxcn.woozoora.ui.base.BaseAdapter
import dev.kxxcn.woozoora.ui.direction.statistic.holder.StatisticBaseHolder
import dev.kxxcn.woozoora.ui.direction.statistic.holder.StatisticCategoryHolder
import dev.kxxcn.woozoora.ui.direction.statistic.holder.StatisticTitleHolder
import dev.kxxcn.woozoora.ui.direction.statistic.holder.StatisticWeeklyHolder
import dev.kxxcn.woozoora.ui.direction.statistic.item.StatisticItem
import dev.kxxcn.woozoora.util.Calculator
import java.lang.ref.WeakReference

class StatisticAdapter(
    private val viewModel: StatisticViewModel,
) : BaseAdapter<StatisticItem, RecyclerView.ViewHolder>(StatisticDiffCallback()) {

    private lateinit var weakReference: WeakReference<RecyclerView>

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        weakReference = WeakReference(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        weakReference.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_TITLE -> StatisticTitleHolder.from(parent)
            TYPE_WEEKLY -> StatisticWeeklyHolder.from(parent)
            else -> StatisticCategoryHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is StatisticBaseHolder) {
            holder.bind(viewModel, getItem(position))
        } else if (holder is StatisticTitleHolder) {
            holder.bind(getItem(position).titleRes)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type
    }

    fun submitListAndScroll(newList: List<StatisticItem>) {
        if (currentList == newList) return
        submitList(newList) {
            weakReference.get()?.let {
                it.scrollToPosition(0)
                it.scheduleLayoutAnimation()
            }
        }
    }

    class StatisticDiffCallback : DiffUtil.ItemCallback<StatisticItem>() {

        override fun areItemsTheSame(oldItem: StatisticItem, newItem: StatisticItem): Boolean {
            return oldItem.transactions == newItem.transactions
        }

        override fun areContentsTheSame(oldItem: StatisticItem, newItem: StatisticItem): Boolean {
            return oldItem == newItem
        }
    }

    companion object {

        private const val TYPE_TITLE = 0

        private const val TYPE_WEEKLY = 1

        private const val TYPE_CATEGORY = 2

        fun create(
            overview: OverviewData,
            userId: String,
            month: Int,
        ): List<StatisticItem> {
            val categoryItems = if (overview.filterTransactionToId(userId).isNotEmpty()) {
                listOf(
                    StatisticItem(
                        TYPE_TITLE,
                        emptyList(),
                        titleRes = R.string.statistics_of_category
                    ),
                    StatisticItem(TYPE_CATEGORY, overview.transactions)
                )
            } else {
                emptyList()
            }

            return listOf(
                StatisticItem(
                    TYPE_TITLE,
                    emptyList(),
                    titleRes = R.string.statistics_of_monthly
                )
            ) + Calculator.calculateRangeByWeek(month).map {
                val transactions = overview.filterTransactionToRange(userId, it)
                StatisticItem(TYPE_WEEKLY, transactions, it)
            } + categoryItems
        }
    }
}
