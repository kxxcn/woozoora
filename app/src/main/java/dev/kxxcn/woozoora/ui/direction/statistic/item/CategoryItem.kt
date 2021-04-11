package dev.kxxcn.woozoora.ui.direction.statistic.item

import dev.kxxcn.woozoora.common.Category
import dev.kxxcn.woozoora.domain.model.TransactionData

data class CategoryItem(
    val category: Category,
    val transactions: List<TransactionData>,
    val usageRate: Float,
) {

    val totalPricing: Int
        get() = transactions.sumBy { it.price }

    val percent: Int
        get() = (usageRate * 100).toInt()
}
