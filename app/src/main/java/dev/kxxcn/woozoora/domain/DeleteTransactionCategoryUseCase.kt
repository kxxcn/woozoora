package dev.kxxcn.woozoora.domain

import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.data.source.DataRepository
import javax.inject.Inject

class DeleteTransactionCategoryUseCase @Inject constructor(
    private val repository: DataRepository,
) {

    suspend operator fun invoke(ids: List<Int>): Result<Int> {
        return repository.deleteTransactionCategory(ids)
    }
}
