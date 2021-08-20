package dev.kxxcn.woozoora.domain

import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.data.source.DataRepository
import javax.inject.Inject

class UpdateCodeUseCase @Inject constructor(
    private val repository: DataRepository,
) {

    suspend operator fun invoke(code: String, isTransfer: Boolean): Result<Any> {
        return repository.updateCode(code, isTransfer)
    }
}
