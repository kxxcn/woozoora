package dev.kxxcn.woozoora.domain

import dev.kxxcn.woozoora.data.source.DataRepository
import javax.inject.Inject

class UpdateNotificationUseCase @Inject constructor(
    private val repository: DataRepository,
) {

    suspend operator fun invoke() {
        repository.updateNotification()
    }
}
