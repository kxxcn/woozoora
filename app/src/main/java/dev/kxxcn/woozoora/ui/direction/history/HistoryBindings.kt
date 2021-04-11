package dev.kxxcn.woozoora.ui.direction.history

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.faltenreich.skeletonlayout.Skeleton
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.domain.model.HistoryData

@BindingAdapter("app:dayItem")
fun setDays(view: RecyclerView, items: List<Pair<Int, Boolean>>?) {
    items?.let {
        val skeleton = view.getTag(R.id.tag_skeleton_history_day_list) as? Skeleton
        if (skeleton != null) {
            skeleton.showOriginal()
            view.setTag(R.id.tag_skeleton_history_day_list, null)
        }
        val days = HistoryDayAdapter.create(it)
        (view.adapter as? HistoryDayAdapter)?.submitListAndScroll(days)
    }
}

@BindingAdapter("app:histories")
fun setTransactions(view: RecyclerView, histories: List<HistoryData>?) {
    histories?.let { newList ->
        val skeleton = view.getTag(R.id.tag_skeleton_history_transaction_list) as? Skeleton
        if (skeleton != null) {
            skeleton.showOriginal()
            view.setTag(R.id.tag_skeleton_history_transaction_list, null)
        }
        val adapter = view.adapter as? HistoryTransactionAdapter
        val updatable = adapter?.isUpdatable(newList) == true
        adapter?.submitList(newList) { if (updatable) view.scheduleLayoutAnimation() }
    }
}
