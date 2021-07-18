package dev.kxxcn.woozoora.ui.direction.statistic

import androidx.lifecycle.*
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import dev.kxxcn.woozoora.common.Event
import dev.kxxcn.woozoora.common.extension.month
import dev.kxxcn.woozoora.common.extension.year
import dev.kxxcn.woozoora.di.AssistedSavedStateViewModelFactory
import dev.kxxcn.woozoora.domain.GetOverviewUseCase
import dev.kxxcn.woozoora.domain.GetUsageTransactionTimeUseCase
import dev.kxxcn.woozoora.domain.model.TimelineData
import dev.kxxcn.woozoora.domain.model.TransactionData
import dev.kxxcn.woozoora.ui.direction.history.HistoryViewModel
import dev.kxxcn.woozoora.ui.direction.home.HomeFilterType
import dev.kxxcn.woozoora.ui.direction.statistic.item.CategoryItem

class StatisticViewModel @AssistedInject constructor(
    getOverviewUseCase: GetOverviewUseCase,
    getUsageTransactionTimeUseCase: GetUsageTransactionTimeUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle,
) : HistoryViewModel(getOverviewUseCase, getUsageTransactionTimeUseCase, savedStateHandle) {

    @AssistedInject.Factory
    interface Factory : AssistedSavedStateViewModelFactory<StatisticViewModel>

    val savedPageState: Int?
        get() = savedStateHandle.get<Int>(CURRENT_PAGE_SAVED_STATE_KEY)

    private val _currentUserId = MutableLiveData<String>()
    val currentUserId: LiveData<String> = _currentUserId

    private val _timelineEvent = MutableLiveData<Event<TimelineData>>()
    val timelineEvent: LiveData<Event<TimelineData>> = _timelineEvent

    val categories = MediatorLiveData<List<CategoryItem>>().apply {
        addSource(overview) {
            val userId = currentUserId.value ?: return@addSource
            val dateTime = dateTimeMs.value ?: return@addSource
            value = it.groupByCategory(
                userId,
                HomeFilterType.MONTHLY,
                dateTime.year,
                dateTime.month
            ).map { (category, transactions) ->
                val usageRate = it.calculateUsageRateByCategory(
                    userId,
                    HomeFilterType.MONTHLY,
                    transactions,
                    dateTime.year,
                    dateTime.month
                )
                CategoryItem(category, transactions, usageRate)
            }
        }
        addSource(currentUserId) {
            val overview = overview.value ?: return@addSource
            val dateTime = dateTimeMs.value ?: return@addSource
            value = overview.groupByCategory(
                it,
                HomeFilterType.MONTHLY,
                dateTime.year,
                dateTime.month
            ).map { (category, transactions) ->
                val usageRate = overview.calculateUsageRateByCategory(
                    it,
                    HomeFilterType.MONTHLY,
                    transactions,
                    dateTime.year,
                    dateTime.month
                )
                CategoryItem(category, transactions, usageRate)
            }
        }
    }

    val isEmpty = categories.map { it.isEmpty() }

    fun setUserId(userId: String?) {
        _currentUserId.value = userId
    }

    fun saveInstanceState(position: Int) {
        savedStateHandle.set(CURRENT_PAGE_SAVED_STATE_KEY, position)
    }

    fun timeline(transactions: List<TransactionData>, range: Pair<Long, Long>) {
        if (transactions.isNotEmpty()) {
            timeline(TimelineData(transactions, range))
        }
    }

    fun timeline(transactions: List<TransactionData>, category: Int) {
        if (transactions.isNotEmpty()) {
            timeline(TimelineData(transactions, category = category))
        }
    }

    private fun timeline(timeline: TimelineData) {
        _timelineEvent.value = Event(timeline)
    }
}

const val CURRENT_PAGE_SAVED_STATE_KEY = "CURRENT_PAGE_SAVED_STATE_KEY"
