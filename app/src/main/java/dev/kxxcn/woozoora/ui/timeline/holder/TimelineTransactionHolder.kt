package dev.kxxcn.woozoora.ui.timeline.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.woozoora.databinding.TimelineTransactionItemBinding
import dev.kxxcn.woozoora.ui.timeline.TimelineViewModel
import dev.kxxcn.woozoora.ui.timeline.item.TimelineItem

class TimelineTransactionHolder(
    private val binding: TimelineTransactionItemBinding,
) : TimelineBaseHolder(binding) {

    override fun bind(viewModel: TimelineViewModel, item: TimelineItem) {
        with(binding) {
            this.lifecycleOwner = this@TimelineTransactionHolder
            this.viewModel = viewModel
            this.item = item
            this.executePendingBindings()
        }
    }

    companion object {

        fun from(parent: ViewGroup): TimelineTransactionHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = TimelineTransactionItemBinding.inflate(inflater, parent, false)
            return TimelineTransactionHolder(binding)
        }
    }
}
