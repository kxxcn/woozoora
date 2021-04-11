package dev.kxxcn.woozoora.ui.timeline.item

import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.FORMAT_DATE_MONTH_DOT_DAY
import dev.kxxcn.woozoora.common.FORMAT_TIME_HOUR_MINUTE
import dev.kxxcn.woozoora.common.extension.hour
import dev.kxxcn.woozoora.common.extension.weekday
import dev.kxxcn.woozoora.domain.model.TransactionData
import dev.kxxcn.woozoora.util.Converter

data class TimelineItem(
    val type: Int,
    val transaction: TransactionData? = null,
    val totalCount: Int = 0,
    val totalSpending: Int = 0,
    val isHeader: Boolean = false,
    val isDashed: Boolean = false
) {

    val date: String?
        get() {
            return transaction
                ?.date
                ?.let { Converter.dateFormat(FORMAT_DATE_MONTH_DOT_DAY, it) }
        }

    val time: String?
        get() {
            return transaction
                ?.date
                ?.let { Converter.dateFormat(FORMAT_TIME_HOUR_MINUTE, it) }
        }

    val weekdayRes: Int?
        get() {
            return transaction
                ?.date
                ?.weekday
                ?.let { Converter.weekdayToResource(it.toFloat()) }
        }

    val timeIcon: Int?
        get() {
            return transaction?.let {
                when {
                    it.date.hour < 7 -> R.drawable.ic_daybreak
                    it.date.hour < 12 -> R.drawable.ic_morning
                    it.date.hour < 18 -> R.drawable.ic_sunset
                    else -> R.drawable.ic_night
                }
            }
        }
}
