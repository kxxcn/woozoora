package dev.kxxcn.woozoora.ui.notification

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.woozoora.common.FORMAT_DATE_YEAR_DOT_MONTH_DOT_DAY
import dev.kxxcn.woozoora.domain.model.NotificationData
import dev.kxxcn.woozoora.ui.base.BaseAdapter
import dev.kxxcn.woozoora.ui.notification.holder.NotificationBaseHolder
import dev.kxxcn.woozoora.ui.notification.holder.NotificationContentHolder
import dev.kxxcn.woozoora.ui.notification.holder.NotificationDateHolder
import dev.kxxcn.woozoora.ui.notification.holder.NotificationEmptyHolder
import dev.kxxcn.woozoora.ui.notification.item.NotificationItem
import dev.kxxcn.woozoora.util.Converter

class NotificationAdapter(
    private val viewModel: NotificationViewModel,
) : BaseAdapter<NotificationItem, RecyclerView.ViewHolder>(NotificationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_EMPTY -> NotificationEmptyHolder.from(parent)
            TYPE_DATE -> NotificationDateHolder.from(parent)
            else -> NotificationContentHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? NotificationBaseHolder)?.bind(viewModel, getItem(position).notification)
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

        private const val TYPE_EMPTY = 0

        private const val TYPE_DATE = 1

        private const val TYPE_CONTENT = 2

        fun create(notifications: List<NotificationData>): List<NotificationItem> {
            return if (notifications.isEmpty()) {
                listOf(NotificationItem(TYPE_EMPTY, NotificationData.empty()))
            } else {
                notifications
                    .groupBy { Converter.dateFormat(FORMAT_DATE_YEAR_DOT_MONTH_DOT_DAY, it.date) }
                    .flatMap { (_, notifications) ->
                        listOf(NotificationItem(TYPE_DATE, notifications.first())) +
                                notifications.map { NotificationItem(TYPE_CONTENT, it) }
                    }
            }
        }
    }
}
