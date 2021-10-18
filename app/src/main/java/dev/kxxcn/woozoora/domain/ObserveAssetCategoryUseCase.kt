package dev.kxxcn.woozoora.domain

import androidx.lifecycle.LiveData
import dev.kxxcn.woozoora.data.source.DataRepository
import dev.kxxcn.woozoora.domain.model.AssetCategoryData
import javax.inject.Inject

class ObserveAssetCategoryUseCase @Inject constructor(
    private val repository: DataRepository,
) {

    operator fun invoke(): LiveData<List<AssetCategoryData>> {
        return repository.observeAssetCategory()
    }
}
