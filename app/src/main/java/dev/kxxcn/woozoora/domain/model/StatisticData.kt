package dev.kxxcn.woozoora.domain.model

import dev.kxxcn.woozoora.common.FORMAT_DATE_YEAR_DOT_MONTH_DOT_DAY
import dev.kxxcn.woozoora.util.Converter
import java.util.*

data class StatisticData(
    val startDate: Long,
    val endDate: Long,
    val date: Long = System.currentTimeMillis(),
    val isChecked: Boolean = false,
    val id: String = UUID.randomUUID().toString(),
    val transactions: List<TransactionData> = emptyList(),
) {

    val dateText: String?
        get() = Converter.dateFormat(FORMAT_DATE_YEAR_DOT_MONTH_DOT_DAY, date)

    val totalPrice = transactions.sumBy { it.price }

    val hasTransactions: Boolean
        get() = transactions.isNotEmpty()

    companion object {

        fun empty(): StatisticData {
            return StatisticData(0, 0)
        }
    }
}
