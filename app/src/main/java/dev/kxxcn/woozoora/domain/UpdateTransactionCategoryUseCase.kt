package dev.kxxcn.woozoora.domain

import dev.kxxcn.woozoora.data.source.DataRepository
import dev.kxxcn.woozoora.data.source.entity.TransactionCategoryEntity
import dev.kxxcn.woozoora.domain.model.TransactionCategoryData
import javax.inject.Inject

class UpdateTransactionCategoryUseCase @Inject constructor(
    private val repository: DataRepository,
) {

    suspend operator fun invoke(list: List<TransactionCategoryData>) {
        return list
            .mapIndexed { i, c -> TransactionCategoryEntity(c.id, c.category, i) }
            .let { repository.updateTransactionCategory(it) }
    }
}
