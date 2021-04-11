package dev.kxxcn.woozoora.ui.direction.statistic.item

import dev.kxxcn.woozoora.common.extension.day
import dev.kxxcn.woozoora.domain.model.TransactionData
import java.util.concurrent.TimeUnit

data class StatisticItem(
    val type: Int,
    val transactions: List<TransactionData>,
    val range: Pair<Long, Long>? = null,
    val titleRes: Int? = null
) {

    private val selector = { transaction: TransactionData -> transaction.price }

    val totalPricing: Int
        get() = transactions.sumBy(selector)

    val eachPricingByDaily: ArrayList<Int>
        get() = if (range == null) {
            arrayListOf()
        } else {
            var targetMs = range.first
            val upperMs = range.second
            arrayListOf<Int>().apply {
                while (targetMs < upperMs) {
                    transactions
                        .filter { it.date.day == targetMs.day }
                        .sumBy(selector)
                        .also { add(it) }
                    targetMs += TimeUnit.DAYS.toMillis(1)
                }
            }
        }

    val maxPricing: Int
        get() = transactions.maxOfOrNull { it.price } ?: 0

    val minPricing: Int
        get() = transactions.minOfOrNull { it.price } ?: 0
}
