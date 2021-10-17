package dev.kxxcn.woozoora.ui.sort

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.woozoora.common.base.ItemTouchHelperCallback
import dev.kxxcn.woozoora.common.base.OnStartDragListener
import dev.kxxcn.woozoora.ui.base.BaseAdapter
import dev.kxxcn.woozoora.ui.sort.item.SortItem
import java.lang.ref.WeakReference
import java.util.*

class SortAdapter(
    private val viewModel: SortViewModel,
) : BaseAdapter<SortItem, SortHolder>(SortDiffCallback()),
    ItemTouchHelperCallback.ItemTouchHelperAdapter, OnStartDragListener {

    private lateinit var refRecyclerView: WeakReference<RecyclerView>

    private lateinit var touchHelper: ItemTouchHelper

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        refRecyclerView = WeakReference(recyclerView)
        val callback = ItemTouchHelperCallback(this)
        touchHelper = ItemTouchHelper(callback).apply { attachToRecyclerView(recyclerView) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SortHolder {
        return SortHolder.from(parent, this)
    }

    override fun onBindViewHolder(holder: SortHolder, position: Int) {
        holder.bind(getItem(position), viewModel)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        val tasks = viewModel.category.value ?: return
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(tasks, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(tasks, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemSelected(viewHolder: RecyclerView.ViewHolder?) {

    }

    override fun onItemClear(viewHolder: RecyclerView.ViewHolder?) {
        viewModel.sort()
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        touchHelper.startDrag(viewHolder)
    }

    class SortDiffCallback : DiffUtil.ItemCallback<SortItem>() {

        override fun areItemsTheSame(oldItem: SortItem, newItem: SortItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SortItem, newItem: SortItem): Boolean {
            return oldItem == newItem
        }
    }
}
