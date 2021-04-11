package dev.kxxcn.woozoora.ui.timeline

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import dev.kxxcn.woozoora.common.Event
import dev.kxxcn.woozoora.common.KEY_TIMELINE_ITEM
import dev.kxxcn.woozoora.common.extension.replace
import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.di.AssistedSavedStateViewModelFactory
import dev.kxxcn.woozoora.domain.GetGroupUseCase
import dev.kxxcn.woozoora.domain.GetUserUseCase
import dev.kxxcn.woozoora.domain.model.HistoryData
import dev.kxxcn.woozoora.domain.model.TimelineData
import dev.kxxcn.woozoora.domain.model.TransactionData
import dev.kxxcn.woozoora.domain.model.UserData
import dev.kxxcn.woozoora.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class TimelineViewModel @AssistedInject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getGroupUseCase: GetGroupUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle,
) : BaseViewModel() {

    @AssistedInject.Factory
    interface Factory : AssistedSavedStateViewModelFactory<TimelineViewModel>

    private val timeline = savedStateHandle.get<TimelineData>(KEY_TIMELINE_ITEM)

    private val _transactions = MutableLiveData<List<TransactionData>>()
    val transactions: LiveData<List<TransactionData>> = _transactions

    private val _receiptEvent = MutableLiveData<Event<HistoryData>>()
    val receiptEvent: LiveData<Event<HistoryData>> = _receiptEvent

    private var cache: List<UserData>? = null

    fun start(transaction: TransactionData?) {
        val currentList = timeline
            ?.transactions
            ?.toList()
            ?: emptyList()

        currentList
            .replace(transaction) { it.id == transaction?.id }
            .filter { timeline?.inPrice(it.price) ?: true }
            .filter { timeline?.inRange(it.date) ?: true }
            .filter { timeline?.inCategory(it.category) ?: true }
            .also { _transactions.value = it }
    }

    fun detail(transaction: TransactionData?) {
        if (cache == null) {
            viewModelScope.launch {
                val result = getGroupUseCase()
                if (result is Result.Success) {
                    cache = result.data
                    receipt(transaction, cache ?: emptyList())
                }
            }
        } else {
            receipt(transaction, cache ?: emptyList())
        }
    }

    fun receipt(transaction: TransactionData?, group: List<UserData>) {
        viewModelScope.launch {
            val result = getUserUseCase()
            if (result is Result.Success) {
                val history = HistoryData(
                    transaction,
                    result.data,
                    group
                )

                _receiptEvent.value = Event(history)
            }
        }
    }
}
