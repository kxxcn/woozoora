package dev.kxxcn.woozoora.util

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.WoozooraActivity
import dev.kxxcn.woozoora.WoozooraApplication
import dev.kxxcn.woozoora.common.*
import dev.kxxcn.woozoora.common.base.BaseCoroutineScope
import dev.kxxcn.woozoora.common.base.NotificationProvider
import dev.kxxcn.woozoora.common.extension.color
import dev.kxxcn.woozoora.data.succeeded
import dev.kxxcn.woozoora.domain.GetNotificationOptionUseCase
import dev.kxxcn.woozoora.domain.GetUserUseCase
import dev.kxxcn.woozoora.domain.SaveNotificationUseCase
import dev.kxxcn.woozoora.domain.model.NotificationData
import dev.kxxcn.woozoora.domain.model.OptionData
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

class NotificationFactory @Inject constructor(
    private val application: WoozooraApplication,
    private val getUserUseCase: GetUserUseCase,
    private val getNotificationOptionUseCase: GetNotificationOptionUseCase,
    private val saveNotificationUseCase: SaveNotificationUseCase,
    private val coroutineScope: BaseCoroutineScope,
) : NotificationProvider.Factory, BaseCoroutineScope by coroutineScope {

    private val notificationManager =
        application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override fun invoke(data: Map<String, String>) {
        sendNotification(data)
    }

    private fun sendNotification(data: Map<String, String>) {
        coroutineScope.launch {
            val channel = data[NOTIFICATION_CHANNEL] ?: CHANNEL_DEFAULT
            val option = OptionData.find(channel) ?: OptionData.DEFAULT

            val isEnabled =
                getNotificationOptionUseCase(option) && getUserUseCase().succeeded

            if (isEnabled) {
                notify(data, option)
                saveNotification(data, option)
            }
        }
    }

    private fun notify(
        data: Map<String, String>,
        option: OptionData,
    ) {
        val groupKey = option.action
        val summaryId = option.requestCode

        val pendingIntent = Intent(
            application,
            WoozooraActivity::class.java
        ).run {
            NavDeepLinkBuilder(application)
                .setGraph(R.navigation.nav_graph)
                .setDestination(R.id.directionFragment)
                .setArguments(
                    bundleOf(
                        KEY_DEFAULT_PAGE to Director.findPage(option),
                        KEY_DEFAULT_DATE to data[NOTIFICATION_TRANSACTION_DATE],
                    )
                )
                .createPendingIntent()
        }

        val notification = NotificationCompat.Builder(application, option.channel)
            .setVibrate(longArrayOf(0, 500))
            .setSmallIcon(R.drawable.ic_woozoora_notification)
            .setColor(application.color(R.color.primaryBlue))
            .setContentTitle(data[NOTIFICATION_TITLE])
            .setContentText(data[NOTIFICATION_BODY])
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(data[NOTIFICATION_BODY]))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setGroup(groupKey)
            .build()

        notificationManager.notify(Random.nextInt(), notification)
        createSummaryNotification(option.channel, groupKey)
            ?.let { notificationManager.notify(summaryId, it) }
    }

    private fun createSummaryNotification(channel: String, groupKey: String?): Notification? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            NotificationCompat.Builder(application, channel)
                .setSmallIcon(R.drawable.ic_woozoora_notification)
                .setColor(application.color(R.color.primaryBlue))
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setGroup(groupKey)
                .setGroupSummary(true)
                .build()
        } else {
            null
        }
    }

    private suspend fun saveNotification(data: Map<String, String>, option: OptionData) {
        option.takeIf { it == OptionData.REGISTRATION }?.let {
            NotificationData(
                userName = data[NOTIFICATION_USER_NAME],
                userProfile = data[NOTIFICATION_USER_PROFILE],
                transactionId = data[NOTIFICATION_TRANSACTION_ID],
                transactionName = data[NOTIFICATION_TRANSACTION_NAME],
                transactionDate = data[NOTIFICATION_TRANSACTION_DATE]?.toLong(),
                transactionPrice = data[NOTIFICATION_TRANSACTION_PRICE]?.toInt(),
                date = data[NOTIFICATION_DATE]?.toLong()
            ).also {
                saveNotificationUseCase(it)
            }
        }
    }
}
