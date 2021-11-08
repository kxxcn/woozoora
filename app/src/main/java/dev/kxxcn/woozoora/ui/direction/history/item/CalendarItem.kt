package dev.kxxcn.woozoora.ui.direction.history.item

import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.extension.day
import dev.kxxcn.woozoora.domain.model.TransactionData

data class CalendarItem(
    val timeMs: Long,
    val transactions: List<TransactionData>
) {

    constructor() : this(-1, emptyList())

    val day: String
        get() = if (isDummy) "" else timeMs.day.toString()

    val colorRes = if (hasTransactions) R.color.black else R.color.grey03

    val spending: Int
        get() = transactions.filter { it.type == 0 }.sumBy { it.price }

    val asset: Int
        get() = transactions.filter { it.type == 1 }.sumBy { it.price }

    val hasSpending: Boolean
        get() = spending != 0

    val hasAsset: Boolean
        get() = asset != 0

    val hasTransactions: Boolean
        get() = transactions.isNotEmpty()

    private val isDummy: Boolean
        get() = timeMs == -1L

    companion object {

        val DUMMY = CalendarItem()
    }
}
