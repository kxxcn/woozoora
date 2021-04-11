package dev.kxxcn.woozoora.domain

import dev.kxxcn.woozoora.data.source.DataRepository
import dev.kxxcn.woozoora.domain.model.NotificationData
import javax.inject.Inject

class SaveNotificationUseCase @Inject constructor(
    private val repository: DataRepository,
) {

    suspend operator fun invoke(notification: NotificationData) {
        repository.saveNotification(notification)
    }
}
