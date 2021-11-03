package dev.kxxcn.woozoora.domain

import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.data.source.DataRepository
import dev.kxxcn.woozoora.domain.model.TransactionData
import javax.inject.Inject

class SaveTransactionUseCase @Inject constructor(
    private val repository: DataRepository
) {

    suspend operator fun invoke(transaction: TransactionData): Result<String?> {
        return repository.saveTransaction(transaction)
    }
}
