package dev.kxxcn.woozoora.ui.timeline.holder

import androidx.databinding.ViewDataBinding
import dev.kxxcn.woozoora.ui.base.BaseHolder
import dev.kxxcn.woozoora.ui.timeline.TimelineViewModel
import dev.kxxcn.woozoora.ui.timeline.item.TimelineItem

abstract class TimelineBaseHolder(binding: ViewDataBinding) : BaseHolder(binding) {

    abstract fun bind(viewModel: TimelineViewModel, item: TimelineItem)
}
