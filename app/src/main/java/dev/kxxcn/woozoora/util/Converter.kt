package dev.kxxcn.woozoora.util

import androidx.databinding.InverseMethod
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.ONE_HUNDREDS_MILLION
import dev.kxxcn.woozoora.common.TEN_THOUSAND
import dev.kxxcn.woozoora.ui.direction.home.HomeFilterType
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object Converter {

    fun moneyToText(money: Long): String {
        if (money == 0L) return "0원"
        return buildString {
            val oneHundredsMillion = money / ONE_HUNDREDS_MILLION
            val tenThousand = (money - oneHundredsMillion * ONE_HUNDREDS_MILLION) / TEN_THOUSAND
            val other =
                (money - oneHundredsMillion * ONE_HUNDREDS_MILLION) - tenThousand * TEN_THOUSAND

            if (oneHundredsMillion != 0L) append("${oneHundredsMillion}억")
            if (tenThousand != 0L) append("${tenThousand}만")
            if (other != 0L) {
                if (tenThousand != 0L || money < 10000L) {
                    append(other)
                }
            }

            append("원")
        }
    }

    @InverseMethod("textFormat")
    fun moneyFormat(text: String?): String? {
        return convertNumberFormat(text)
    }

    fun textFormat(money: String?): String? {
        return money?.replace(",", "")
    }

    @InverseMethod("numberFormat")
    fun moneyFormat(number: Int?): String? {
        return convertNumberFormat(number.toString())
    }

    fun numberFormat(money: String?): Int? {
        return money?.takeIf { it.isNotEmpty() }?.replace(",", "")?.toInt()
    }

    fun rangeOfHomeFilterType(
        filterType: HomeFilterType,
        year: Int? = null,
        month: Int? = null,
        day: Int? = null,
    ): Pair<Long, Long> {
        return Calculator.calculateRange(
            filterType,
            year,
            month,
            day
        )
    }

    fun dateFormat(pattern: String, timeMs: Long?): String? {
        return timeMs?.let { SimpleDateFormat(pattern, Locale.KOREA).format(it) }
    }

    fun dateParse(pattern: String, source: String?): Date? {
        return source?.let { SimpleDateFormat(pattern, Locale.KOREA).parse(it) }
    }

    fun weekdayToResource(weekday: Float): Int {
        return when (weekday.toInt()) {
            Calendar.SUNDAY -> R.string.sunday_short
            Calendar.MONDAY -> R.string.monday_short
            Calendar.TUESDAY -> R.string.tuesday_short
            Calendar.WEDNESDAY -> R.string.wednesday_short
            Calendar.THURSDAY -> R.string.thursday_short
            Calendar.FRIDAY -> R.string.friday_short
            else -> R.string.saturday_short
        }
    }

    fun indexToResource(index: Int): Int {
        return when (index) {
            0 -> R.string.monday_short
            1 -> R.string.tuesday_short
            2 -> R.string.wednesday_short
            3 -> R.string.thursday_short
            4 -> R.string.friday_short
            5 -> R.string.saturday_short
            else -> R.string.sunday_short
        }
    }

    private fun convertNumberFormat(text: String?): String? {
        return try {
            val number = text
                ?.replace(",", "")
                ?.toInt()
            val decimalFormat = NumberFormat.getInstance(Locale.KOREA)
            decimalFormat.format(number)
        } catch (e: Exception) {
            text
        }
    }
}
