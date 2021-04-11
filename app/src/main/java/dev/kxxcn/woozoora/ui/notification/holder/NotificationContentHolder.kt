package dev.kxxcn.woozoora.ui.notification.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.woozoora.databinding.NotificationContentItemBinding
import dev.kxxcn.woozoora.domain.model.NotificationData
import dev.kxxcn.woozoora.ui.notification.NotificationViewModel

class NotificationContentHolder(
    private val binding: NotificationContentItemBinding,
) : NotificationBaseHolder(binding) {

    override fun bind(viewModel: NotificationViewModel, notification: NotificationData) {
        with(binding) {
            this.lifecycleOwner = this@NotificationContentHolder
            this.viewModel = viewModel
            this.notification = notification
            this.executePendingBindings()
        }
    }

    companion object {

        fun from(parent: ViewGroup): NotificationContentHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = NotificationContentItemBinding.inflate(inflater, parent, false)
            return NotificationContentHolder(binding)
        }
    }
}
