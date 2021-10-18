package dev.kxxcn.woozoora.domain

import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.data.source.DataRepository
import javax.inject.Inject

class SaveAssetCategoryUseCase @Inject constructor(
    private val repository: DataRepository,
) {

    suspend operator fun invoke(category: String): Result<Any> {
        return repository.saveAssetCategory(category)
    }
}
