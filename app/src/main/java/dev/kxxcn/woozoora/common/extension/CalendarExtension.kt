package dev.kxxcn.woozoora.common.extension

import java.util.*

fun Calendar.asBegin(): Calendar {
    return apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
}

fun Calendar.asEnd(): Calendar {
    return apply {
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
        set(Calendar.MILLISECOND, 59)
    }
}

fun Calendar.copy(): Calendar {
    return Calendar.getInstance().apply { timeInMillis = this@copy.timeInMillis }
}

fun Calendar.asDailyNotification(): Calendar {
    val dailyNotificationHourOfDay = 20
    val startTheNextDay = get(Calendar.HOUR_OF_DAY) >= dailyNotificationHourOfDay
    return apply {
        set(Calendar.HOUR_OF_DAY, dailyNotificationHourOfDay)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
        add(Calendar.DAY_OF_YEAR, if (startTheNextDay) 1 else 0)
    }
}

fun Calendar.asWeeklyNotification(): Calendar {
    val randomHour = Random().nextInt(5) + 15
    val randomMinute = Random().nextInt(60)
    return apply {
        add(Calendar.DAY_OF_YEAR, 7)
        set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        set(Calendar.HOUR_OF_DAY, randomHour)
        set(Calendar.MINUTE, randomMinute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
}

val Calendar.year: Int
    get() = get(Calendar.YEAR)

val Calendar.month: Int
    get() = get(Calendar.MONTH)

val Calendar.day: Int
    get() = get(Calendar.DAY_OF_MONTH)
