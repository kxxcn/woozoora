package dev.kxxcn.woozoora.domain

import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.data.source.DataRepository
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val repository: DataRepository,
) {

    suspend operator fun invoke(sponsorId: String, isTransfer: Boolean): Result<String?> {
        return repository.updateUser(sponsorId, isTransfer)
    }

    suspend operator fun invoke(budget: Long): Result<Any> {
        return repository.updateUser(budget)
    }

    suspend operator fun invoke(year: Int): Result<Any> {
        return repository.updateUser(year)
    }
}
