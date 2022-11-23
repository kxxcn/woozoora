package dev.kxxcn.woozoora.ui.receipt

import androidx.lifecycle.*
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.Event
import dev.kxxcn.woozoora.common.KEY_HISTORY_ITEM
import dev.kxxcn.woozoora.data.ifSucceeded
import dev.kxxcn.woozoora.data.succeeded
import dev.kxxcn.woozoora.di.AssistedSavedStateViewModelFactory
import dev.kxxcn.woozoora.domain.DeleteTransactionUseCase
import dev.kxxcn.woozoora.domain.GetUsageTransactionTimeUseCase
import dev.kxxcn.woozoora.domain.IsEnableEditAdsUseCase
import dev.kxxcn.woozoora.domain.model.HistoryData
import dev.kxxcn.woozoora.domain.model.TransactionData
import dev.kxxcn.woozoora.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class ReceiptViewModel @AssistedInject constructor(
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val getUsageTransactionTimeUseCase: GetUsageTransactionTimeUseCase,
    private val isEnableEditAdsUseCase: IsEnableEditAdsUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle,
) : BaseViewModel() {

    @AssistedInject.Factory
    interface Factory : AssistedSavedStateViewModelFactory<ReceiptViewModel>

    val history = savedStateHandle.get<HistoryData>(KEY_HISTORY_ITEM)

    val isEditable = history?.isEditable

    private val _editEvent = MutableLiveData<Event<HistoryData>>()
    val editEvent: LiveData<Event<HistoryData>> = _editEvent

    private val _transactionEvent = MutableLiveData<TransactionData?>()
    val transactionEvent: LiveData<TransactionData?> = _transactionEvent

    private val _deleteEvent = MutableLiveData<Event<Unit>>()
    val deleteEvent: LiveData<Event<Unit>> = _deleteEvent

    val usageTransactionTime = liveData { emit(getUsageTransactionTimeUseCase()) }

    val isProgress = _transactionEvent.map { it != null }

    fun edit() {
        history?.let { _editEvent.value = Event(it) }
    }

    fun saveStateHandle() {
        if (history?.isNew == true) {
            history.transaction
        } else {
            null
        }.also {
            _transactionEvent.value = it
        }
    }

    fun ask() {
        _deleteEvent.value = Event(Unit)
    }

    fun delete() {
        viewModelScope.launch {
            deleteTransactionUseCase(history?.transaction)
                .ifSucceeded {
                    toast(R.string.account_book_has_been_deleted)
                    saveStateHandle(history?.transaction?.copy(price = -1))
                }
        }
    }

    fun isEnabledAds(): Boolean {
        val result = isEnableEditAdsUseCase()
        return history?.isNew == true && result.succeeded
    }

    private fun saveStateHandle(transactionData: TransactionData?) {
        _transactionEvent.value = transactionData
    }
}
