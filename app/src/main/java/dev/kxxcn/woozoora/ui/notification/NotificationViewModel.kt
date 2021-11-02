package dev.kxxcn.woozoora.ui.notification

import androidx.lifecycle.*
import dev.kxxcn.woozoora.common.FORMAT_DATE_YEAR_DOT_MONTH_DOT_DAY
import dev.kxxcn.woozoora.domain.GetNotificationsUseCase
import dev.kxxcn.woozoora.domain.GetStatisticUseCase
import dev.kxxcn.woozoora.domain.UpdateNotificationUseCase
import dev.kxxcn.woozoora.domain.UpdateStatisticUseCase
import dev.kxxcn.woozoora.domain.model.NotificationData
import dev.kxxcn.woozoora.domain.model.StatisticData
import dev.kxxcn.woozoora.ui.base.BaseViewModel
import dev.kxxcn.woozoora.ui.notification.item.NotificationItem
import dev.kxxcn.woozoora.util.Converter
import kotlinx.coroutines.launch
import javax.inject.Inject

class NotificationViewModel @Inject constructor(
    getNotificationsUseCase: GetNotificationsUseCase,
    getStatisticUseCase: GetStatisticUseCase,
    private val updateNotificationUseCase: UpdateNotificationUseCase,
    private val updateStatisticUseCase: UpdateStatisticUseCase,
) : BaseViewModel() {

    val notifications: LiveData<List<NotificationData>> =
        getNotificationsUseCase().distinctUntilChanged()

    val statistics: LiveData<List<StatisticData>> =
        liveData { emit(getStatisticUseCase().distinctUntilChanged()) }.switchMap { it }

    val items = MediatorLiveData<List<NotificationItem>>().apply {
        addSource(notifications) { n ->
            val s = statistics.value ?: emptyList()
            value = createNotificationItems(n, s)
        }
        addSource(statistics) { s ->
            val n = notifications.value ?: emptyList()
            value = createNotificationItems(n, s)
        }
    }

    fun updateNotifications() {
        viewModelScope.launch {
            updateNotificationUseCase()
        }
    }

    fun updateStatistics() {
        viewModelScope.launch {
            updateStatisticUseCase()
        }
    }

    private fun createNotificationItems(
        notifications: List<NotificationData>,
        statistics: List<StatisticData>
    ): List<NotificationItem> {
        return if (notifications.isEmpty() && statistics.isEmpty()) {
            listOf(NotificationItem(NotificationAdapter.TYPE_EMPTY, NotificationData.empty()))
        } else {
            notifications
                .groupBy { Converter.dateFormat(FORMAT_DATE_YEAR_DOT_MONTH_DOT_DAY, it.date) }
                .flatMap { (_, list) ->
                    listOf(
                        NotificationItem(
                            NotificationAdapter.TYPE_DATE,
                            list.first()
                        )
                    ) + list.map {
                        NotificationItem(
                            NotificationAdapter.TYPE_CONTENT,
                            it
                        )
                    }
                } + statistics
                .filter { it.hasTransactions }
                .map {
                    NotificationItem(
                        NotificationAdapter.TYPE_STATISTIC,
                        statistic = it
                    )
                }.sortedByDescending { it.date }
        }
    }
}
