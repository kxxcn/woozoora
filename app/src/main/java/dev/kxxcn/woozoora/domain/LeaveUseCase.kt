package dev.kxxcn.woozoora.domain

import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.data.source.DataRepository
import javax.inject.Inject

class LeaveUseCase @Inject constructor(
    private val repository: DataRepository,
) {

    suspend operator fun invoke(): Result<Any> {
        return repository.leave()
    }
}
