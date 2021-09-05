package dev.kxxcn.woozoora.ui.direction.history

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.woozoora.common.base.IncomparableDiffCallback
import dev.kxxcn.woozoora.domain.model.HistoryData
import dev.kxxcn.woozoora.ui.base.BaseAdapter
import dev.kxxcn.woozoora.ui.direction.history.holder.HistoryEmptyHolder
import dev.kxxcn.woozoora.ui.direction.history.holder.HistoryTransactionHolder

class HistoryTransactionAdapter(
    private val viewModel: HistoryViewModel,
) : BaseAdapter<HistoryData, RecyclerView.ViewHolder>(IncomparableDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_EMPTY -> HistoryEmptyHolder.from(parent)
            else -> HistoryTransactionHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HistoryTransactionHolder) {
            holder.bind(getItem(position), viewModel)
        } else if (holder is HistoryEmptyHolder) {
            holder.bind()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getViewType(position)
    }

    private fun getViewType(position: Int): Int {
        return if (getItem(position).transaction == null) TYPE_EMPTY else TYPE_VIEW
    }

    companion object {

        const val TYPE_EMPTY = 0

        const val TYPE_VIEW = 1
    }
}
