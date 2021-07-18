package dev.kxxcn.woozoora.domain

import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.data.source.DataRepository
import javax.inject.Inject

class SaveUsageTransactionTimeUseCase @Inject constructor(
    private val repository: DataRepository,
) {

    suspend operator fun invoke(value: Boolean): Result<Boolean> {
        return repository.saveUsageTransactionTime(value)
    }
}
