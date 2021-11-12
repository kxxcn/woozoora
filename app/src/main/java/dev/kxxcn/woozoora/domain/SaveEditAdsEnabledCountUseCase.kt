package dev.kxxcn.woozoora.domain

import dev.kxxcn.woozoora.data.source.DataRepository
import javax.inject.Inject

class SaveEditAdsEnabledCountUseCase @Inject constructor(
    private val repository: DataRepository
) {

    operator fun invoke(count: Long) {
        repository.saveEditAdsEnabledCount(count)
    }
}
