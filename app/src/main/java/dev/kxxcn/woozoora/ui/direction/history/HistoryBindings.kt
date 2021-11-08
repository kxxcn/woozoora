package dev.kxxcn.woozoora.ui.direction.history

import android.widget.GridLayout
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.faltenreich.skeletonlayout.Skeleton
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.domain.model.HistoryData
import dev.kxxcn.woozoora.ui.custom.HistoryCalendarView
import dev.kxxcn.woozoora.ui.direction.history.item.CalendarItem
import dev.kxxcn.woozoora.ui.direction.history.item.DayItem

@BindingAdapter("app:selectors")
fun setSelectors(view: RecyclerView, items: List<DayItem>?) {
    items?.let {
        val skeleton = view.getTag(R.id.tag_skeleton_history_day_list) as? Skeleton
        if (skeleton != null) {
            skeleton.showOriginal()
            view.setTag(R.id.tag_skeleton_history_day_list, null)
        }
        (view.adapter as? HistoryDayAdapter)?.submitListAndScroll(it)
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

@BindingAdapter("app:days")
fun setDays(view: GridLayout, days: List<CalendarItem>?) {
    days?.let {
        view.removeAllViews()
        days.map {
            HistoryCalendarView(view.context)
                .apply { bind(it) }
                .apply {
                    layoutParams = GridLayout.LayoutParams()
                        .apply {
                            columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                            rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                        }
                }
        }.forEach {
            view.addView(it)
        }
    }
}
