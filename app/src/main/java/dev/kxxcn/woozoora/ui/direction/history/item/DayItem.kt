package dev.kxxcn.woozoora.ui.direction.history.item

import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.domain.model.TransactionData

data class DayItem(
    val day: Int,
    val transaction: List<TransactionData>,
) {

    private val allSpending: Boolean
        get() = transaction.all { it.type == 0 }

    private val allAsset: Boolean
        get() = transaction.all { it.type == 1 }

    val spending: Int
        get() = transaction.filter { it.type == 0 }.sumBy { it.price }

    val asset: Int
        get() = transaction.filter { it.type == 1 }.sumBy { it.price }

    val hasSpending: Boolean
        get() = spending != 0 && !allSpendingOrAllAsset

    val hasAsset: Boolean
        get() = asset != 0 && !allSpendingOrAllAsset

    val hasTransaction: Boolean
        get() = transaction.isNotEmpty()

    val allSpendingOrAllAsset: Boolean
        get() = hasTransaction && (allSpending || allAsset)

    val spendingOrAsset: Int
        get() = if (allSpending) spending else asset

    val colorRes: Int
        get() = if (allSpending) R.color.primaryBlack else R.color.green02

    val stringRes: Int
        get() = if (allSpending) R.string.format_price_minus else R.string.format_price_plus

    fun calculateColorRes(isSelected: Boolean): Int {
        return if (allSpending) {
            if (isSelected) R.color.white01 else R.color.primaryBlack
        } else {
            R.color.green02
        }
    }
}
