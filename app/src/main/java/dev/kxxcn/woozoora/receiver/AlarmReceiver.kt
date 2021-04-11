package dev.kxxcn.woozoora.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import dagger.android.DaggerBroadcastReceiver
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.*
import dev.kxxcn.woozoora.common.base.BaseCoroutineScope
import dev.kxxcn.woozoora.common.base.NotificationProvider
import dev.kxxcn.woozoora.common.extension.setAlarmToFireWhenTheDeviceIsIdle
import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.domain.GetOverviewUseCase
import dev.kxxcn.woozoora.domain.model.OptionData
import dev.kxxcn.woozoora.ui.direction.home.HomeFilterType
import dev.kxxcn.woozoora.util.Converter
import kotlinx.coroutines.launch
import javax.inject.Inject

class AlarmReceiver : DaggerBroadcastReceiver() {

    @Inject
    lateinit var notification: NotificationProvider.Factory

    @Inject
    lateinit var getOverviewUseCase: GetOverviewUseCase

    @Inject
    lateinit var coroutineScope: BaseCoroutineScope

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        val triggerAtMillis = intent?.getLongExtra(EXTRA_TRIGGER_TIME, 0L) ?: 0L

        context?.let {
            when (intent?.action) {
                Intent.ACTION_BOOT_COMPLETED -> rescheduleAlarms(it)
                ACTION_CODE_NOTIFICATION_DAILY -> actionNotificationDaily(it, triggerAtMillis)
                ACTION_CODE_NOTIFICATION_WEEKLY -> actionNotificationWeekly(it, triggerAtMillis)
            }
        }
    }

    private fun rescheduleAlarms(context: Context) {
        scheduleRepeatingAlarms(context, OptionData.DAILY)
        scheduleRepeatingAlarms(context, OptionData.WEEKLY)
    }

    private fun actionNotificationDaily(context: Context, triggerAtMillis: Long) {
        mapOf(
            NOTIFICATION_CHANNEL to CHANNEL_DAILY,
            NOTIFICATION_TITLE to context.getString(R.string.try_to_record_your_spending_today)
        ).also { data ->
            notification(data)
        }

        scheduleRepeatingAlarms(
            context,
            OptionData.DAILY,
            triggerAtMillis + AlarmManager.INTERVAL_DAY
        )
    }

    private fun actionNotificationWeekly(context: Context, triggerAtMillis: Long) {
        coroutineScope.launch {
            val result = getOverviewUseCase()
            if (result is Result.Success) {
                val overview = result.data
                val range = Converter.rangeOfHomeFilterType(HomeFilterType.WEEKLY)
                val startDate = Converter.dateFormat(FORMAT_DATE_MONTH_DOT_DAY, range.first)
                val endDate = Converter.dateFormat(FORMAT_DATE_MONTH_DOT_DAY, range.second)
                val totalSpending = overview.getTotalSpendingByRange(
                    overview.id,
                    range
                )

                val contentTitle = context.getString(
                    R.string.format_weekly_notification_title,
                    startDate,
                    endDate
                )
                val contentBody = buildString {
                    append(
                        context.getString(R.string.format_spending,
                            Converter.moneyFormat(totalSpending)
                        )
                    )
                    overview.groupByCategory(overview.id, HomeFilterType.WEEKLY)
                        .map { (category, transactions) ->
                            appendLine()
                            val categoryName = context.getString(category.nameRes)
                            val spending = transactions.sumBy { it.price }
                            append(
                                context.getString(R.string.format_category,
                                    categoryName,
                                    Converter.moneyFormat(spending),
                                    transactions.size)
                            )
                        }
                }
                mapOf(
                    NOTIFICATION_CHANNEL to CHANNEL_WEEKLY,
                    NOTIFICATION_TITLE to contentTitle,
                    NOTIFICATION_BODY to contentBody
                ).also { data ->
                    notification(data)
                }
            }
        }

        scheduleRepeatingAlarms(
            context,
            OptionData.WEEKLY,
            triggerAtMillis + INTERVAL_WEEK
        )
    }

    private fun scheduleRepeatingAlarms(
        context: Context,
        option: OptionData,
        nextTriggerAtMillis: Long? = null,
    ) {
        with(context) {
            val triggerAtMillis = nextTriggerAtMillis ?: option.triggerAtMillis
            val pendingIntent = createPendingIntent(this, option, triggerAtMillis)
            setAlarmToFireWhenTheDeviceIsIdle(pendingIntent, triggerAtMillis)
        }
    }

    private fun createPendingIntent(
        context: Context,
        option: OptionData,
        triggerAtMillis: Long,
    ): PendingIntent {
        val intent = Intent(
            context,
            AlarmReceiver::class.java
        ).apply {
            action = option.action
            putExtra(EXTRA_TRIGGER_TIME, triggerAtMillis)
        }

        return PendingIntent.getBroadcast(
            context,
            option.requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}
