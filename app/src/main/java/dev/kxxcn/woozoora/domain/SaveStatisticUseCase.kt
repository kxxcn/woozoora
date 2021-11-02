package dev.kxxcn.woozoora.domain

import dev.kxxcn.woozoora.data.source.DataRepository
import dev.kxxcn.woozoora.domain.model.StatisticData
import javax.inject.Inject

class SaveStatisticUseCase @Inject constructor(
    private val repository: DataRepository,
) {

    suspend operator fun invoke(statistic: StatisticData) {
        repository.saveStatistic(statistic)
    }
}
