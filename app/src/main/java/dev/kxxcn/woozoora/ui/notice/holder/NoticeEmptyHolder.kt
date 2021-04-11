package dev.kxxcn.woozoora.ui.notice.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.woozoora.databinding.NoticeEmptyItemBinding
import dev.kxxcn.woozoora.domain.model.NoticeData
import dev.kxxcn.woozoora.ui.notice.NoticeViewModel

class NoticeEmptyHolder(
    private val binding: NoticeEmptyItemBinding,
) : NoticeBaseHolder(binding) {

    override fun bind(viewModel: NoticeViewModel, notice: NoticeData, isExpand: Boolean) {
        with(binding) {
            this.lifecycleOwner = this@NoticeEmptyHolder
            this.executePendingBindings()
        }
    }

    companion object {

        fun from(parent: ViewGroup): NoticeEmptyHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = NoticeEmptyItemBinding.inflate(inflater, parent, false)
            return NoticeEmptyHolder(binding)
        }
    }
}
