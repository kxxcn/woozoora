package dev.kxxcn.woozoora.ui.date

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.woozoora.databinding.DatePickerItemBinding
import dev.kxxcn.woozoora.ui.base.BaseHolder
import dev.kxxcn.woozoora.ui.date.item.DatePickerItem

class DatePickerHolder(
    private val binding: DatePickerItemBinding
) : BaseHolder(binding) {

    fun bind(item: DatePickerItem, viewModel: DatePickerViewModel) {
        with(binding) {
            this.lifecycleOwner = this@DatePickerHolder
            this.item = item
            this.viewModel = viewModel
            this.executePendingBindings()
        }
    }

    companion object {

        fun from(parent: ViewGroup): DatePickerHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = DatePickerItemBinding.inflate(inflater, parent, false)
            return DatePickerHolder(binding)
        }
    }
}
