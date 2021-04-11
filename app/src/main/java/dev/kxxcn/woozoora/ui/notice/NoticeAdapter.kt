package dev.kxxcn.woozoora.ui.notice

import android.util.SparseBooleanArray
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.woozoora.domain.model.NoticeData
import dev.kxxcn.woozoora.ui.base.BaseAdapter
import dev.kxxcn.woozoora.ui.notice.holder.NoticeBaseHolder
import dev.kxxcn.woozoora.ui.notice.holder.NoticeContentHolder
import dev.kxxcn.woozoora.ui.notice.holder.NoticeEmptyHolder
import dev.kxxcn.woozoora.ui.notice.item.NoticeItem

class NoticeAdapter(
    private val viewModel: NoticeViewModel,
) : BaseAdapter<NoticeItem, RecyclerView.ViewHolder>(NoticeDiffCallback()) {

    private val expandable = SparseBooleanArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_EMPTY -> NoticeEmptyHolder.from(parent)
            else -> NoticeContentHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val notice = getItem(position).notice
        (holder as? NoticeBaseHolder)?.bind(viewModel, notice, isExpand(position))
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type
    }

    fun save(pos: Int, isExpand: Boolean) {
        expandable.put(pos, isExpand)
    }

    private fun isExpand(position: Int): Boolean {
        return expandable[position]
    }

    class NoticeDiffCallback : DiffUtil.ItemCallback<NoticeItem>() {

        override fun areItemsTheSame(oldData: NoticeItem, newData: NoticeItem): Boolean {
            return oldData.notice.id == newData.notice.id
        }

        override fun areContentsTheSame(oldData: NoticeItem, newData: NoticeItem): Boolean {
            return oldData == newData
        }
    }

    companion object {

        private const val TYPE_EMPTY = 0

        private const val TYPE_CONTENT = 1

        fun create(notices: List<NoticeData>): List<NoticeItem> {
            return if (notices.isEmpty()) {
                listOf(NoticeItem(TYPE_EMPTY))
            } else {
                notices.map { NoticeItem(TYPE_CONTENT, it) }
            }
        }
    }
}
