package dev.kxxcn.woozoora.ui.notification

import androidx.lifecycle.*
import dev.kxxcn.woozoora.domain.GetNotificationsUseCase
import dev.kxxcn.woozoora.domain.GetStatisticUseCase
import dev.kxxcn.woozoora.domain.UpdateNotificationUseCase
import dev.kxxcn.woozoora.domain.UpdateStatisticUseCase
import dev.kxxcn.woozoora.ui.base.BaseViewModel
import dev.kxxcn.woozoora.ui.notification.item.NotificationItem
import kotlinx.coroutines.launch
import javax.inject.Inject

class NotificationViewModel @Inject constructor(
    getNotificationsUseCase: GetNotificationsUseCase,
    getStatisticUseCase: GetStatisticUseCase,
    private val updateNotificationUseCase: UpdateNotificationUseCase,
    private val updateStatisticUseCase: UpdateStatisticUseCase,
) : BaseViewModel() {

    val notifications: LiveData<List<NotificationItem>> =
        getNotificationsUseCase().distinctUntilChanged().map { data ->
            data.groupBy { it.compare }
                .flatMap { (_, list) ->
                    listOf(
                        NotificationItem(
                            NotificationAdapter.TYPE_DATE,
                            list.first()
                        )
                    ) + list.map {
                        NotificationItem(
                            NotificationAdapter.TYPE_NOTIFICATION,
                            it
                        )
                    }
                }
        }

    val statistics: LiveData<List<NotificationItem>> =
        liveData { emit(getStatisticUseCase().distinctUntilChanged()) }
            .switchMap { it }
            .map { data ->
                data.filter { it.hasTransactions }
                    .map {
                        NotificationItem(
                            NotificationAdapter.TYPE_STATISTIC,
                            statistic = it
                        )
                    }
            }

    val items = MediatorLiveData<List<NotificationItem>>().apply {
        addSource(notifications) { n ->
            val s = statistics.value ?: emptyList()
            value = (n + s).sortedByDescending { it.compare }
        }
        addSource(statistics) { s ->
            val n = notifications.value ?: emptyList()
            value = (n + s).sortedByDescending { it.compare }
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
}
