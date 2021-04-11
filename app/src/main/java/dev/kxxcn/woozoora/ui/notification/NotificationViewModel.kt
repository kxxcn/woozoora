package dev.kxxcn.woozoora.ui.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import dev.kxxcn.woozoora.domain.GetNotificationsUseCase
import dev.kxxcn.woozoora.domain.UpdateNotificationUseCase
import dev.kxxcn.woozoora.domain.model.NotificationData
import dev.kxxcn.woozoora.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class NotificationViewModel @Inject constructor(
    getNotificationsUseCase: GetNotificationsUseCase,
    private val updateNotificationUseCase: UpdateNotificationUseCase,
) : BaseViewModel() {

    val notifications: LiveData<List<NotificationData>> =
        getNotificationsUseCase().distinctUntilChanged()

    fun update() {
        viewModelScope.launch {
            updateNotificationUseCase()
        }
    }
}
