package dev.kxxcn.woozoora.ui.edit

import android.text.InputType
import dev.kxxcn.woozoora.R

enum class EditFilterType(
    val titleRes: Int,
    val hintRes: Int,
    val inputType: Int
) {

    NAME(
        R.string.transaction_name_no_spacing,
        R.string.enter_the_content,
        InputType.TYPE_CLASS_TEXT
    ),

    DATE(
        R.string.date,
        R.string.enter_the_date,
        InputType.TYPE_CLASS_TEXT
    ),

    TIME(
        R.string.time,
        R.string.enter_the_time,
        InputType.TYPE_CLASS_TEXT
    ),

    PRICE(
        R.string.transaction_price_no_spacing,
        R.string.enter_the_price,
        InputType.TYPE_CLASS_NUMBER
    ),

    DESCRIPTION(
        R.string.memo,
        R.string.enter_the_memo,
        InputType.TYPE_CLASS_TEXT
    );

    fun needToInputScreen(): Boolean {
        return this in setOf(NAME, PRICE, DESCRIPTION)
    }
}
