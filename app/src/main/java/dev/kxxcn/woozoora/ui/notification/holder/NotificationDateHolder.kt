package dev.kxxcn.woozoora.ui.notification.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.woozoora.databinding.NotificationDateItemBinding
import dev.kxxcn.woozoora.domain.model.NotificationData
import dev.kxxcn.woozoora.ui.notification.NotificationViewModel

class NotificationDateHolder(
    private val binding: NotificationDateItemBinding,
) : NotificationBaseHolder(binding) {

    override fun bind(viewModel: NotificationViewModel, notification: NotificationData) {
        with(binding) {
            this.lifecycleOwner = this@NotificationDateHolder
            this.viewModel = viewModel
            this.notification = notification
            this.executePendingBindings()
        }
    }

    companion object {

        fun from(parent: ViewGroup): NotificationDateHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = NotificationDateItemBinding.inflate(inflater, parent, false)
            return NotificationDateHolder(binding)
        }
    }
}
