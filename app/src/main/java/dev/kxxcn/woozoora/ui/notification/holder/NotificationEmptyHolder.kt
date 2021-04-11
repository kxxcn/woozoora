package dev.kxxcn.woozoora.ui.notification.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.woozoora.databinding.NotificationEmptyItemBinding
import dev.kxxcn.woozoora.domain.model.NotificationData
import dev.kxxcn.woozoora.ui.notification.NotificationViewModel

class NotificationEmptyHolder(
    private val binding: NotificationEmptyItemBinding,
) : NotificationBaseHolder(binding) {

    override fun bind(viewModel: NotificationViewModel, notification: NotificationData) {
        with(binding) {
            this.lifecycleOwner = this@NotificationEmptyHolder
            this.executePendingBindings()
        }
    }

    companion object {

        fun from(parent: ViewGroup): NotificationEmptyHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = NotificationEmptyItemBinding.inflate(inflater, parent, false)
            return NotificationEmptyHolder(binding)
        }
    }
}
