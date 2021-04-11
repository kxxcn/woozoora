package dev.kxxcn.woozoora.common.extension

import java.util.*

val Long.year: Int
    get() = Calendar.getInstance()
        .apply { timeInMillis = this@year }
        .run { get(Calendar.YEAR) }

val Long.month: Int
    get() = Calendar.getInstance()
        .apply { timeInMillis = this@month }
        .run { get(Calendar.MONTH) }

val Long.day: Int
    get() = Calendar.getInstance()
        .apply { timeInMillis = this@day }
        .run { get(Calendar.DAY_OF_MONTH) }

val Long.weekday: Int
    get() = Calendar.getInstance()
        .apply { timeInMillis = this@weekday }
        .run { get(Calendar.DAY_OF_WEEK) }

val Long.hour: Int
    get() = Calendar.getInstance()
        .apply { timeInMillis = this@hour }
        .run { get(Calendar.HOUR_OF_DAY) }
