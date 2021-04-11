package dev.kxxcn.woozoora.ui.notification.item

import dev.kxxcn.woozoora.domain.model.NotificationData

data class NotificationItem(
    val type: Int,
    val notification: NotificationData,
)
