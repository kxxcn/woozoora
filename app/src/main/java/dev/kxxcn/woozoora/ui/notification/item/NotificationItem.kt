package dev.kxxcn.woozoora.ui.notification.item

import dev.kxxcn.woozoora.domain.model.NotificationData
import dev.kxxcn.woozoora.domain.model.StatisticData
import java.util.*

data class NotificationItem(
    val type: Int,
    val notification: NotificationData = NotificationData.empty(),
    val statistic: StatisticData = StatisticData.empty(),
) {

    val date: Long
        get() = notification.transactionDate ?: statistic.date

    val compare: Long
        get() = Calendar.getInstance().apply {
            timeInMillis = date
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.run {
            timeInMillis
        }
}
