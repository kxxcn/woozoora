package dev.kxxcn.woozoora.ui.timeline.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.woozoora.databinding.TimelineTotalItemBinding
import dev.kxxcn.woozoora.ui.timeline.TimelineViewModel
import dev.kxxcn.woozoora.ui.timeline.item.TimelineItem

class TimelineTotalHolder(
    private val binding: TimelineTotalItemBinding,
) : TimelineBaseHolder(binding) {

    override fun bind(viewModel: TimelineViewModel, item: TimelineItem) {
        with(binding) {
            this.lifecycleOwner = this@TimelineTotalHolder
            this.item = item
            this.executePendingBindings()
        }
    }

    companion object {

        fun from(parent: ViewGroup): TimelineTotalHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = TimelineTotalItemBinding.inflate(inflater, parent, false)
            return TimelineTotalHolder(binding)
        }
    }
}
