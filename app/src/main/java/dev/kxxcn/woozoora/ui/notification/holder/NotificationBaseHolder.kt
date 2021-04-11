package dev.kxxcn.woozoora.ui.notification.holder

import androidx.databinding.ViewDataBinding
import dev.kxxcn.woozoora.domain.model.NotificationData
import dev.kxxcn.woozoora.ui.base.BaseHolder
import dev.kxxcn.woozoora.ui.notification.NotificationViewModel

abstract class NotificationBaseHolder(binding: ViewDataBinding) : BaseHolder(binding) {

    abstract fun bind(viewModel: NotificationViewModel, notification: NotificationData)
}
