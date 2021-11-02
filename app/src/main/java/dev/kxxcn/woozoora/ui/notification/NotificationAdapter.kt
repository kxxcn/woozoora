package dev.kxxcn.woozoora.ui.notification

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.woozoora.ui.base.BaseAdapter
import dev.kxxcn.woozoora.ui.notification.holder.*
import dev.kxxcn.woozoora.ui.notification.item.NotificationItem

class NotificationAdapter(
    private val viewModel: NotificationViewModel,
) : BaseAdapter<NotificationItem, RecyclerView.ViewHolder>(NotificationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_EMPTY -> NotificationEmptyHolder.from(parent)
            TYPE_DATE -> NotificationDateHolder.from(parent)
            TYPE_STATISTIC -> NotificationChartHolder.from(parent)
            else -> NotificationContentHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NotificationBaseHolder -> holder.bind(viewModel, getItem(position).notification)
            is NotificationChartHolder -> holder.bind(viewModel, getItem(position).statistic)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type
    }

    class NotificationDiffCallback : DiffUtil.ItemCallback<NotificationItem>() {

        override fun areItemsTheSame(
            oldItem: NotificationItem,
            newItem: NotificationItem,
        ): Boolean {
            return oldItem.notification.id == newItem.notification.id
        }

        override fun areContentsTheSame(
            oldItem: NotificationItem,
            newItem: NotificationItem,
        ): Boolean {
            return oldItem == newItem
        }
    }

    companion object {

        const val TYPE_EMPTY = 0

        const val TYPE_DATE = 1

        const val TYPE_CONTENT = 2

        const val TYPE_STATISTIC = 3
    }
}
