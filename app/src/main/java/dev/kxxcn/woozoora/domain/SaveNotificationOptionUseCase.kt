package dev.kxxcn.woozoora.domain

import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.data.source.DataRepository
import dev.kxxcn.woozoora.domain.model.OptionData
import javax.inject.Inject

class SaveNotificationOptionUseCase @Inject constructor(
    private val repository: DataRepository,
) {

    suspend operator fun invoke(
        option: OptionData,
        value: Boolean,
    ): Result<Boolean> {
        return repository.saveNotificationOption(option, value)
    }
}
