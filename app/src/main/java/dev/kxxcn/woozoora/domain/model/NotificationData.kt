package dev.kxxcn.woozoora.domain.model

import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.FORMAT_DATE_TIME_DOT
import dev.kxxcn.woozoora.common.FORMAT_DATE_YEAR_DOT_MONTH_DOT_DAY
import dev.kxxcn.woozoora.util.Converter
import java.util.*

data class NotificationData(
    val id: String = "",
    val userName: String? = null,
    val userProfile: String? = null,
    val transactionId: String? = null,
    val transactionName: String? = null,
    val transactionDate: Long? = null,
    val transactionPrice: Int? = null,
    val transactionType: Int? = null,
    val date: Long? = null,
    val isChecked: Boolean = false,
) {

    val dateText: String?
        get() = Converter.dateFormat(FORMAT_DATE_YEAR_DOT_MONTH_DOT_DAY, transactionDate)

    val transactionDateText: String?
        get() = Converter.dateFormat(FORMAT_DATE_TIME_DOT, transactionDate)

    val transactionNameTextRes: Int
        get() = if (transactionType == 0) R.string.format_price_minus else R.string.format_price_plus

    val transactionNameColorRes: Int
        get() = if (transactionType == 0) R.color.primaryBlack else R.color.green02

    val priceText: String?
        get() = Converter.moneyFormat(transactionPrice)

    val compare: Long
        get() = Calendar.getInstance().apply {
            timeInMillis = transactionDate ?: 0
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.run {
            timeInMillis
        }

    companion object {

        fun empty(): NotificationData {
            return NotificationData()
        }
    }
}
