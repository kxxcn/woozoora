package dev.kxxcn.woozoora.domain

import dev.kxxcn.woozoora.data.source.DataRepository
import dev.kxxcn.woozoora.data.source.entity.AssetCategoryEntity
import dev.kxxcn.woozoora.domain.model.AssetCategoryData
import javax.inject.Inject

class UpdateAssetCategoryUseCase @Inject constructor(
    private val repository: DataRepository,
) {

    suspend operator fun invoke(list: List<AssetCategoryData>) {
        return list
            .mapIndexed { i, c -> AssetCategoryEntity(c.id, c.category, i) }
            .let { repository.updateAssetCategory(it) }
    }
}
