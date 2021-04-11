package dev.kxxcn.woozoora.common

import dev.kxxcn.woozoora.R

enum class Payment(
    val nameRes: Int,
    val colorRes: Int
) {

    CASH(R.string.cash, R.color.paymentCash),

    CARD(R.string.card, R.color.paymentCard);

    companion object {

        fun find(ordinary: Int): Payment {
            return values()[ordinary]
        }
    }
}
