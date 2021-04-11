package dev.kxxcn.woozoora.ui.notice.holder

import androidx.databinding.ViewDataBinding
import dev.kxxcn.woozoora.domain.model.NoticeData
import dev.kxxcn.woozoora.ui.base.BaseHolder
import dev.kxxcn.woozoora.ui.notice.NoticeViewModel

abstract class NoticeBaseHolder(binding: ViewDataBinding) : BaseHolder(binding) {

    abstract fun bind(viewModel: NoticeViewModel, notice: NoticeData, isExpand: Boolean)
}
