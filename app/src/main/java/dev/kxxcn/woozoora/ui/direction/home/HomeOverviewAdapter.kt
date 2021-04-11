package dev.kxxcn.woozoora.ui.direction.home

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.woozoora.common.base.Animatable
import dev.kxxcn.woozoora.domain.model.OverviewData
import dev.kxxcn.woozoora.ui.base.BaseAdapter
import dev.kxxcn.woozoora.ui.direction.home.holder.*
import dev.kxxcn.woozoora.ui.direction.home.item.OverviewItem
import java.lang.ref.WeakReference

class HomeOverviewAdapter(
    private val viewModel: HomeViewModel,
) : BaseAdapter<OverviewItem, RecyclerView.ViewHolder>(OverviewDiffCallback()) {

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
            TYPE_STICK -> HomeStickHolder.from(parent)
            TYPE_USER_TITLE -> HomeTitleHolder.from(parent)
            TYPE_CATEGORY -> HomeCategoryHolder.from(parent)
            TYPE_BUDGET -> HomeBudgetHolder.from(parent)
            TYPE_PAYMENT -> HomePaymentHolder.from(parent)
            TYPE_GROUP_TITLE -> HomeTitleHolder.from(parent)
            TYPE_GROUP -> HomeGroupHolder.from(parent)
            TYPE_EVENT -> HomeEventHolder.from(parent)
            TYPE_EMPTY -> HomeEmptyHolder.from(parent)
            else -> HomeGroupHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position).overview
        when (holder) {
            is HomeTitleHolder -> if (isGroupTitle(position)) holder.bindGroupTitle() else holder.bindUserTitle()
            is HomeCategoryHolder -> holder.bind(item, viewModel)
            is HomeBudgetHolder -> holder.bind(item, viewModel)
            is HomePaymentHolder -> holder.bind(item, viewModel)
            is HomeGroupHolder -> holder.bind(item, viewModel)
            is HomeEventHolder -> holder.bind(item, viewModel)
            is HomeEmptyHolder -> holder.bind(viewModel)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        (holder as? Animatable)?.clearAnimation()
    }

    fun submitListAndScroll(newList: List<OverviewItem>) {
        submitList(newList) { weakReference.get()?.scrollToPosition(0) }
    }

    private fun isGroupTitle(position: Int): Boolean {
        return position == TYPE_GROUP_TITLE
    }

    class OverviewDiffCallback : DiffUtil.ItemCallback<OverviewItem>() {

        override fun areItemsTheSame(oldItem: OverviewItem, newItem: OverviewItem): Boolean {
            return oldItem.overview.transactions == newItem.overview.transactions
                    && oldItem.overview.user == newItem.overview.user
        }

        override fun areContentsTheSame(oldItem: OverviewItem, newItem: OverviewItem): Boolean {
            return oldItem == newItem
        }
    }

    companion object {

        private const val TYPE_STICK = 0

        private const val TYPE_USER_TITLE = 1

        private const val TYPE_BUDGET = 2

        private const val TYPE_CATEGORY = 3

        private const val TYPE_PAYMENT = 4

        private const val TYPE_GROUP_TITLE = 5

        private const val TYPE_GROUP = 6

        private const val TYPE_EVENT = 7

        private const val TYPE_EMPTY = 8

        private const val TYPE_TRANSACTION = 9

        fun create(overview: OverviewData): List<OverviewItem> {
            val overviewRelatedToGroup = if (overview.hasGroup) {
                val eventOverview = if (overview.hasTransactions) {
                    listOf(OverviewItem(TYPE_EVENT, overview))
                } else {
                    emptyList()
                }
                listOf(
                    OverviewItem(TYPE_GROUP_TITLE, overview),
                    OverviewItem(TYPE_GROUP, overview),
                ) + eventOverview
            } else {
                listOf(OverviewItem(TYPE_EMPTY, overview))
            }

            return listOf(
                OverviewItem(TYPE_STICK, overview),
                OverviewItem(TYPE_USER_TITLE, overview),
                OverviewItem(TYPE_BUDGET, overview),
                OverviewItem(TYPE_CATEGORY, overview),
                OverviewItem(TYPE_PAYMENT, overview)
            ) + overviewRelatedToGroup
        }
    }
}
