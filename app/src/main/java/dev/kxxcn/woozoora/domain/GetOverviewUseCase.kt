package dev.kxxcn.woozoora.domain

import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.data.source.DataRepository
import dev.kxxcn.woozoora.domain.model.OverviewData
import javax.inject.Inject

class GetOverviewUseCase @Inject constructor(
    private val repository: DataRepository
) {

    suspend operator fun invoke(
        year: Int? = null,
        month: Int? = null
    ): Result<OverviewData> {
        return repository.getOverview(year, month)
    }
}
