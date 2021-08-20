package dev.kxxcn.woozoora.ui.direction.more

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.woozoora.common.base.IncomparableDiffCallback
import dev.kxxcn.woozoora.ui.base.BaseAdapter
import dev.kxxcn.woozoora.ui.direction.more.holder.*

class MoreAdapter(
    private val viewModel: MoreViewModel,
) : BaseAdapter<Int, RecyclerView.ViewHolder>(IncomparableDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_APPLICATION -> MoreApplicationHolder.from(parent)
            TYPE_GROUP -> MoreGroupHolder.from(parent)
            TYPE_NOTIFICATION -> MoreNotificationHolder.from(parent)
            TYPE_POLICY -> MorePolicyHolder.from(parent)
            else -> MoreHelpHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? MoreBaseHolder)?.bind(viewModel)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position)
    }

    companion object {

        private const val TYPE_APPLICATION = 0
        private const val TYPE_GROUP = 1
        private const val TYPE_NOTIFICATION = 2
        private const val TYPE_POLICY = 3
        private const val TYPE_HELP = 4

        fun create(): List<Int> {
            return listOf(
                TYPE_APPLICATION,
                TYPE_GROUP,
                TYPE_NOTIFICATION,
                TYPE_POLICY,
                TYPE_HELP,
            )
        }
    }
}
