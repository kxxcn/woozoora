package dev.kxxcn.woozoora.domain

import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.data.source.DataRepository
import javax.inject.Inject

class IsEnableEditAdsUseCase @Inject constructor(
    private val repository: DataRepository,
) {

    operator fun invoke(): Result<Unit> {
        return repository.isEnableEditAds()
    }
}
