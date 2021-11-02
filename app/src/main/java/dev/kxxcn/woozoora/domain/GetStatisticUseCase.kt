package dev.kxxcn.woozoora.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.data.flatMap
import dev.kxxcn.woozoora.data.getContentIfSucceeded
import dev.kxxcn.woozoora.data.source.DataRepository
import dev.kxxcn.woozoora.domain.model.StatisticData
import javax.inject.Inject

class GetStatisticUseCase @Inject constructor(
    private val repository: DataRepository,
) {

    suspend operator fun invoke(): LiveData<List<StatisticData>> {
        val result = repository.getStatistics()

        val overview = try {
            result.flatMap { statistic ->
                val minDate = statistic.minOf { it.startDate }
                val maxDate = statistic.maxOf { it.endDate }
                repository.getOverview(minDate, maxDate)
            }
        } catch (e: Exception) {
            Result.Error(e)
        }

        val statistics = result.getContentIfSucceeded
            ?.map { statistic ->
                val range = statistic.startDate to statistic.endDate
                val transactions = overview
                    .getContentIfSucceeded
                    ?.filterTransactionToRange(range = range)
                    ?: emptyList()

                statistic.copy(transactions = transactions)
            } ?: emptyList()

        return MutableLiveData(statistics)
    }
}
