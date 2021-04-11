package dev.kxxcn.woozoora.domain.model

import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.*
import dev.kxxcn.woozoora.common.extension.asDailyNotification
import dev.kxxcn.woozoora.common.extension.asWeeklyNotification
import java.util.*

enum class OptionData(
    val channel: String,
    val nameRes: Int,
    val requestCode: Int,
    val action: String,
) {

    DEFAULT(
        CHANNEL_DEFAULT,
        R.string.default_notification,
        REQUEST_CODE_NOTIFICATION_DEFAULT,
        ACTION_CODE_NOTIFICATION_DEFAULT,
    ),

    REGISTRATION(
        CHANNEL_REGISTRATION,
        R.string.register_transaction_notification,
        REQUEST_CODE_NOTIFICATION_REGISTRATION,
        ACTION_CODE_NOTIFICATION_REGISTRATION,
    ),

    DAILY(
        CHANNEL_DAILY,
        R.string.daily_transaction_notification,
        REQUEST_CODE_NOTIFICATION_DAILY,
        ACTION_CODE_NOTIFICATION_DAILY,
    ),

    WEEKLY(
        CHANNEL_WEEKLY,
        R.string.weekly_transaction_notification,
        REQUEST_CODE_NOTIFICATION_WEEKLY,
        ACTION_CODE_NOTIFICATION_WEEKLY,
    ),

    NOTICE(
        CHANNEL_NOTICE,
        R.string.notice_notification,
        REQUEST_CODE_NOTIFICATION_NOTICE,
        ACTION_CODE_NOTIFICATION_NOTICE,
    );

    val triggerAtMillis: Long
        get() = when (this) {
            DAILY -> Calendar.getInstance().asDailyNotification().timeInMillis
            else -> Calendar.getInstance().asWeeklyNotification().timeInMillis
        }

    companion object {

        fun find(channel: String?): OptionData? {
            return values().find { it.channel == channel }
        }
    }
}
