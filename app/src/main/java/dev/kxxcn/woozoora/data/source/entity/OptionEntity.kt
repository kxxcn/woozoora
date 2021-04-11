package dev.kxxcn.woozoora.data.source.entity

import dev.kxxcn.woozoora.common.*

enum class OptionEntity(
    val key: String,
) {
    DEFAULT(PREF_NOTIFICATION_DEFAULT),

    REGISTRATION(PREF_NOTIFICATION_REGISTRATION),

    DAILY(PREF_NOTIFICATION_DAILY),

    WEEKLY(PREF_NOTIFICATION_WEEKLY),

    NOTICE(PREF_NOTIFICATION_NOTICE)
}
