package dev.kxxcn.woozoora.domain

import androidx.lifecycle.LiveData
import dev.kxxcn.woozoora.data.source.DataRepository
import dev.kxxcn.woozoora.domain.model.NotificationData
import javax.inject.Inject

class GetNotificationsUseCase @Inject constructor(
    private val repository: DataRepository,
) {

    operator fun invoke(): LiveData<List<NotificationData>> {
        return repository.getNotifications()
    }
}
