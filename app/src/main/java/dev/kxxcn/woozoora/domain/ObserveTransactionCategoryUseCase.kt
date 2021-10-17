package dev.kxxcn.woozoora.domain

import androidx.lifecycle.LiveData
import dev.kxxcn.woozoora.data.source.DataRepository
import dev.kxxcn.woozoora.domain.model.TransactionCategoryData
import javax.inject.Inject

class ObserveTransactionCategoryUseCase @Inject constructor(
    private val repository: DataRepository,
) {

    operator fun invoke(): LiveData<List<TransactionCategoryData>> {
        return repository.observeTransactionCategory()
    }
}
