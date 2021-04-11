package dev.kxxcn.woozoora.domain

import dev.kxxcn.woozoora.data.source.DataRepository
import dev.kxxcn.woozoora.domain.model.OptionData
import javax.inject.Inject

class GetNotificationOptionUseCase @Inject constructor(
    private val repository: DataRepository,
) {

    suspend operator fun invoke(option: OptionData): Boolean {
        return repository.getNotificationOption(option)
    }
}
