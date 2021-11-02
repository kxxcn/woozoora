package dev.kxxcn.woozoora.ui.notification.item

import dev.kxxcn.woozoora.domain.model.NotificationData
import dev.kxxcn.woozoora.domain.model.StatisticData

data class NotificationItem(
    val type: Int,
    val notification: NotificationData = NotificationData.empty(),
    val statistic: StatisticData = StatisticData.empty(),
) {

    val date: Long
        get() = notification.date ?: statistic.date
}
