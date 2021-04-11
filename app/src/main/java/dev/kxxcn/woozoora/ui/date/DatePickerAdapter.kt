package dev.kxxcn.woozoora.ui.date

import android.view.ViewGroup
import dev.kxxcn.woozoora.common.base.IncomparableDiffCallback
import dev.kxxcn.woozoora.ui.base.BaseAdapter
import dev.kxxcn.woozoora.ui.date.item.DatePickerItem

class DatePickerAdapter(
    private val viewModel: DatePickerViewModel
) : BaseAdapter<DatePickerItem, DatePickerHolder>(IncomparableDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatePickerHolder {
        return DatePickerHolder.from(parent)
    }

    override fun onBindViewHolder(holder: DatePickerHolder, position: Int) {
        holder.bind(getItem(position), viewModel)
    }
}
