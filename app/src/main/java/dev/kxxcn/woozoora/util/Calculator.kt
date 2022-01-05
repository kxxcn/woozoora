package dev.kxxcn.woozoora.util

import dev.kxxcn.woozoora.common.extension.asBegin
import dev.kxxcn.woozoora.common.extension.asEnd
import dev.kxxcn.woozoora.common.extension.copy
import dev.kxxcn.woozoora.common.extension.day
import dev.kxxcn.woozoora.ui.direction.home.HomeFilterType
import java.util.*
import java.util.concurrent.TimeUnit

object Calculator {

    fun calculateCurrentYear(): Int {
        return Calendar.getInstance().get(Calendar.YEAR)
    }

    fun calculateCurrentMonth(): Int {
        return Calendar.getInstance().get(Calendar.MONTH) + 1
    }

    fun calculateRange(
        filterType: HomeFilterType,
        year: Int? = null,
        month: Int? = null,
        day: Int? = null,
    ): Pair<Long, Long> {
        return when (filterType) {
            HomeFilterType.DAILY -> {
                val startDate = Calendar.getInstance()
                    .asBegin()
                    .run { timeInMillis }

                val endDate = Calendar.getInstance()
                    .asEnd()
                    .run { timeInMillis }

                startDate to endDate
            }
            HomeFilterType.WEEKLY -> {
                val calendar = Calendar.getInstance()
                    .apply { year?.let { set(Calendar.YEAR, it) } }
                    .apply { month?.let { set(Calendar.MONTH, it) } }
                    .apply { day?.let { set(Calendar.DAY_OF_MONTH, it) } }

                val lowerMs = calendar.copy()
                    .apply { set(Calendar.DAY_OF_MONTH, 1) }
                    .asBegin()
                    .run { timeInMillis }

                val upperMs = calendar.copy()
                    .apply { set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH)) }
                    .asEnd()
                    .run { timeInMillis }

                val (startMs, endMs) = if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    val endMs = calendar.timeInMillis
                    calendar.add(Calendar.DATE, -6)
                    val startMs = calendar.timeInMillis
                    startMs to endMs
                } else {
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
                    val startMs = calendar.timeInMillis
                    calendar.add(Calendar.DATE, 6)
                    val endMs = calendar.timeInMillis
                    startMs to endMs
                }

                val startDate = calendar
                    .apply { timeInMillis = startMs.takeIf { it >= lowerMs } ?: lowerMs }
                    .asBegin()
                    .run { timeInMillis }

                val endDate = calendar
                    .apply { timeInMillis = endMs.takeIf { it <= upperMs } ?: upperMs }
                    .asEnd()
                    .run { timeInMillis }

                startDate to endDate
            }
            HomeFilterType.MONTHLY -> {
                val calendar = Calendar.getInstance()
                    .apply { year?.let { set(Calendar.YEAR, it) } }
                    .apply { month?.let { set(Calendar.MONTH, it) } }

                val startDate = calendar
                    .asBegin()
                    .apply { set(Calendar.DAY_OF_MONTH, 1) }
                    .run { timeInMillis }

                val endDate = calendar
                    .asEnd()
                    .apply { set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH)) }
                    .run { timeInMillis }

                startDate to endDate
            }
        }
    }

    fun calculateRangeForDays(filterType: HomeFilterType): Pair<Int, Int> {
        val (startDate, endDate) = calculateRange(filterType)
        val c = Calendar.getInstance()
        return c.apply { timeInMillis = startDate }.day to c.apply { timeInMillis = endDate }.day
    }

    fun calculateRangeByWeek(
        year: Int = Calendar.getInstance().get(Calendar.YEAR),
        month: Int = Calendar.getInstance().get(Calendar.MONTH),
    ): MutableList<Pair<Long, Long>> {
        val upperMs = Calendar.getInstance()
            .apply { set(Calendar.YEAR, year) }
            .apply { set(Calendar.MONTH, month) }
            .apply { set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH)) }
            .asEnd()
            .run { timeInMillis }

        val ranges = mutableListOf<Pair<Long, Long>>()
        val calendar = Calendar.getInstance()
            .apply { set(Calendar.YEAR, year) }
            .apply { set(Calendar.MONTH, month) }
            .apply { set(Calendar.DAY_OF_MONTH, 1) }

        do {
            val range = calculateRange(
                HomeFilterType.WEEKLY,
                year = calendar.get(Calendar.YEAR),
                month = calendar.get(Calendar.MONTH),
                day = calendar.get(Calendar.DAY_OF_MONTH)
            )
            ranges.add(range)
            with(calendar) {
                timeInMillis = range.second
                add(Calendar.DAY_OF_MONTH, 1)
                get(Calendar.DAY_OF_MONTH)
            }
        } while (range.second < upperMs)

        return ranges
    }

    fun calculateDaysOfWeekday(): MutableMap<Int, List<Long>> {
        val days = mutableMapOf<Int, List<Long>>()
        calculateRangeByWeek().mapIndexed { index, (startMs, endMs) ->
            val values = mutableListOf<Long>()
            var targetMs = startMs
            while (targetMs < endMs) {
                values.add(targetMs)
                targetMs += TimeUnit.DAYS.toMillis(1)
            }
            days[index + 1] = values
        }
        return days
    }
}
