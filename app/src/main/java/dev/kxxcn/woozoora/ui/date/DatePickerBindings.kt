package dev.kxxcn.woozoora.ui.date

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.woozoora.common.FORMAT_DATE_YEAR_AND_MONTH
import dev.kxxcn.woozoora.ui.date.item.DatePickerItem
import dev.kxxcn.woozoora.util.Converter

@BindingAdapter("app:datePicker")
fun setDatePickerList(view: RecyclerView, items: List<DatePickerItem>?) {
    items?.let { newList ->
        (view.adapter as? DatePickerAdapter)?.submitList(newList) {
            items.withIndex()
                .find { it.value.isSelected }
                ?.index
                ?.let { view.scrollToPosition(it) }
        }
    }
}

@BindingAdapter("app:datePickerText")
fun setDatePickerText(view: TextView, timeMs: Long) {
    view.text = Converter.dateFormat(FORMAT_DATE_YEAR_AND_MONTH, timeMs)
}
