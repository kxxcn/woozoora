package dev.kxxcn.woozoora.domain

import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.data.source.DataRepository
import dev.kxxcn.woozoora.domain.model.AssetCategoryData
import javax.inject.Inject

class GetAssetCategoryUseCase @Inject constructor(
    private val repository: DataRepository,
) {

    suspend operator fun invoke(): Result<List<AssetCategoryData>> {
        return repository.getAssetCategory()
    }
}
