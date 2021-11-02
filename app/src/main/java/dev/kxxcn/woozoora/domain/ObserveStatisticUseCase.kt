package dev.kxxcn.woozoora.domain

import androidx.lifecycle.LiveData
import dev.kxxcn.woozoora.data.source.DataRepository
import dev.kxxcn.woozoora.domain.model.StatisticData
import javax.inject.Inject

class ObserveStatisticUseCase @Inject constructor(
    private val repository: DataRepository,
) {

    operator fun invoke(): LiveData<List<StatisticData>> {
        return repository.observeStatistics()
    }
}
