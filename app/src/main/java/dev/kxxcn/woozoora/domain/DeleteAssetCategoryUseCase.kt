package dev.kxxcn.woozoora.domain

import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.data.source.DataRepository
import javax.inject.Inject

class DeleteAssetCategoryUseCase @Inject constructor(
    private val repository: DataRepository,
) {

    suspend operator fun invoke(ids: List<String>): Result<Int> {
        return repository.deleteAssetCategory(ids)
    }
}
