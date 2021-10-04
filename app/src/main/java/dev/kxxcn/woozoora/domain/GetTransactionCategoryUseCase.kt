package dev.kxxcn.woozoora.domain

import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.data.source.DataRepository
import dev.kxxcn.woozoora.domain.model.TransactionCategoryData
import javax.inject.Inject

class GetTransactionCategoryUseCase @Inject constructor(
    private val repository: DataRepository,
) {

    suspend operator fun invoke(): Result<List<TransactionCategoryData>> {
        return repository.getTransactionCategory()
    }
}
