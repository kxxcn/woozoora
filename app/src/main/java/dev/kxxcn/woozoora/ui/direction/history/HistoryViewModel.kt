package dev.kxxcn.woozoora.ui.direction.history

import androidx.lifecycle.*
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import dev.kxxcn.woozoora.common.Event
import dev.kxxcn.woozoora.common.extension.*
import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.di.AssistedSavedStateViewModelFactory
import dev.kxxcn.woozoora.domain.GetOverviewUseCase
import dev.kxxcn.woozoora.domain.GetUsageTransactionTimeUseCase
import dev.kxxcn.woozoora.domain.model.HistoryData
import dev.kxxcn.woozoora.domain.model.OverviewData
import dev.kxxcn.woozoora.domain.model.TransactionData
import dev.kxxcn.woozoora.ui.base.BaseViewModel
import dev.kxxcn.woozoora.ui.direction.history.item.CalendarItem
import dev.kxxcn.woozoora.ui.direction.history.item.DayItem
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

open class HistoryViewModel @AssistedInject constructor(
    private val getOverviewUseCase: GetOverviewUseCase,
    private val getUsageTransactionTimeUseCase: GetUsageTransactionTimeUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle,
) : BaseViewModel(savedStateHandle) {

    @AssistedInject.Factory
    interface Factory : AssistedSavedStateViewModelFactory<HistoryViewModel>

    val selectedDay = MediatorLiveData<Int>().apply {
        addSource(dateTimeMs) { value = it.day }
    }

    private val forceUpdate = MutableLiveData<Unit>()

    val usageTransactionTime = liveData { emit(getUsageTransactionTimeUseCase()) }

    val overview = MediatorLiveData<OverviewData>().apply {
        addSource(forceUpdate) {
            val dateTime = dateTimeMs.value ?: return@addSource
            viewModelScope.launch {
                val result = getOverviewUseCase(dateTime.year, dateTime.month)
                if (result is Result.Success) {
                    value = result.data
                }
            }
        }
        addSource(dateTimeMs) {
            viewModelScope.launch {
                val result = getOverviewUseCase(it.year, it.month)
                if (result is Result.Success) {
                    value = result.data
                }
            }
        }
    }

    val selectors = overview.map {
        dateTimeMs.value?.let { getMaximumDay(it.year, it.month) }
    }

    val histories = MediatorLiveData<List<HistoryData>?>().apply {
        addSource(selectedDay) { value = createHistory(it, overview.value) }
        addSource(overview) { value = createHistory(selectedDay.value, it) }
    }

    val days = MediatorLiveData<List<CalendarItem>>().apply {
        addSource(dateTimeMs) { value = createCalendarItems(it, overview.value) }
        addSource(overview) { value = createCalendarItems(dateTimeMs.value, it) }
    }

    val stopEvent = histories.map { Event(it != null) }

    private val _receiptEvent = MutableLiveData<Event<HistoryData>>()
    val receiptEvent: LiveData<Event<HistoryData>> = _receiptEvent

    val scrollEvent = selectedDay.map { it - 1 }

    fun start(timeMs: Long? = null) {
        timeMs?.let { selectDate(it) }
        forceUpdate.value = Unit
    }

    fun select(day: Int?) {
        day?.let { selectedDay.value = it }
    }

    fun select() {
        selectedDay.value = selectedDay.value
    }

    fun receipt(history: HistoryData) {
        _receiptEvent.value = Event(history)
    }

    private fun transactionIfMatchedDay(day: Int): List<TransactionData> {
        return overview.value?.transactions
            ?.filter { it.date.day == day }
            ?: emptyList()
    }

    private fun getMaximumDay(year: Int, month: Int): List<DayItem> {
        val maximum = Calendar.getInstance()
            .apply { set(Calendar.YEAR, year) }
            .apply { set(Calendar.MONTH, month) }
            .getActualMaximum(Calendar.DAY_OF_MONTH)
        return IntRange(1, maximum).map { DayItem(it, transactionIfMatchedDay(it)) }
    }

    private fun createHistory(
        day: Int?,
        overview: OverviewData?,
    ): List<HistoryData>? {
        return if (day == null || overview == null) {
            null
        } else {
            overview.transactions
                .filter { it.date.day == day }
                .sortedByDescending { it.date }
                .map { HistoryData(it, overview.user, overview.group) }
                .takeIf { it.isNotEmpty() }
                ?: listOf(HistoryData(null, overview.user, overview.group))
        }
    }

    private fun createCalendarItems(
        timeMs: Long?,
        overviewData: OverviewData?
    ): List<CalendarItem> {
        return if (timeMs == null || overviewData == null) {
            emptyList()
        } else {
            val lowerMs = Calendar.getInstance()
                .apply { timeInMillis = timeMs }
                .apply { set(Calendar.DAY_OF_MONTH, 1) }
                .asBegin()
                .run { timeInMillis }

            val upperMs = Calendar.getInstance()
                .apply { timeInMillis = timeMs }
                .apply { set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH)) }
                .asEnd()
                .run { timeInMillis }

            val days = calculateDaysToMillis(lowerMs, upperMs)

            createCalendarItems(days, overviewData.transactions)
        }
    }

    private fun calculateDaysToMillis(
        lowerMs: Long,
        upperMs: Long,
        acc: List<Long> = emptyList()
    ): List<Long> {
        return when {
            lowerMs > upperMs -> acc
            else -> calculateDaysToMillis(
                lowerMs + TimeUnit.DAYS.toMillis(1),
                upperMs,
                listOf(*acc.toTypedArray(), lowerMs)
            )
        }
    }

    private fun createCalendarItems(
        days: List<Long>,
        transactions: List<TransactionData>,
        count: Int = days.first().weekday - 1,
        acc: List<CalendarItem> = emptyList()
    ): List<CalendarItem> {
        return when (count) {
            0 -> {
                acc + days.map { timeMs ->
                    CalendarItem(
                        timeMs,
                        transactions.filter { it.date.day == timeMs.day }
                    )
                }
            }
            else -> {
                createCalendarItems(
                    days,
                    transactions,
                    count - 1,
                    listOf(*acc.toTypedArray(), CalendarItem.DUMMY)
                )
            }
        }
    }
}
