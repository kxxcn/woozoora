package dev.kxxcn.woozoora.domain.model

import dev.kxxcn.woozoora.common.FORMAT_DATE_YEAR_DOT_MONTH_DOT_DAY
import dev.kxxcn.woozoora.util.Converter

data class ReplyData(
    val message: String,
    val date: Long,
) {

    val dateText: String?
        get() = Converter.dateFormat(FORMAT_DATE_YEAR_DOT_MONTH_DOT_DAY, date)
}
