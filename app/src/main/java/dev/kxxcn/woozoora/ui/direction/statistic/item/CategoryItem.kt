package dev.kxxcn.woozoora.ui.direction.statistic.item

import dev.kxxcn.woozoora.domain.model.TransactionData
import dev.kxxcn.woozoora.util.ColorGenerator

data class CategoryItem(
    val category: String?,
    val transactions: List<TransactionData>,
    val usageRate: Float,
) {

    val totalPricing: Int
        get() = transactions.sumBy { it.price }

    val percent: Int
        get() = (usageRate * 100).toInt()

    val colorRes: Int
        get() = ColorGenerator.random()
}
