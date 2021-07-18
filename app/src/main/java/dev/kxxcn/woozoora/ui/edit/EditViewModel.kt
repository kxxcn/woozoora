package dev.kxxcn.woozoora.ui.edit

import androidx.lifecycle.*
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.*
import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.data.succeeded
import dev.kxxcn.woozoora.di.AssistedSavedStateViewModelFactory
import dev.kxxcn.woozoora.domain.GetUsageTransactionTimeUseCase
import dev.kxxcn.woozoora.domain.GetUserUseCase
import dev.kxxcn.woozoora.domain.SaveTransactionUseCase
import dev.kxxcn.woozoora.domain.model.HistoryData
import dev.kxxcn.woozoora.domain.model.TransactionData
import dev.kxxcn.woozoora.ui.base.MotionViewModel
import dev.kxxcn.woozoora.util.Converter
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class EditViewModel @AssistedInject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val saveTransactionUseCase: SaveTransactionUseCase,
    private val getUsageTransactionTimeUseCase: GetUsageTransactionTimeUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle,
) : MotionViewModel(savedStateHandle) {

    @AssistedInject.Factory
    interface Factory : AssistedSavedStateViewModelFactory<EditViewModel>

    val history = savedStateHandle.get<HistoryData>(KEY_HISTORY_ITEM)

    val isEditable = history != null

    val editName = MutableLiveData<String?>().apply {
        value = history?.transaction?.name
    }

    val isEmptyName = editName.map { it == null || it.isEmpty() }

    val editDate = MutableLiveData<String?>().apply {
        val date = history?.date ?: run {
            val timeMs = Calendar.getInstance().timeInMillis
            Converter.dateFormat(FORMAT_DATE_YEAR_DOT_MONTH_DOT_DAY, timeMs)
        }
        value = date
    }

    val isEmptyDate = editDate.map { it == null || it.isEmpty() }

    val editTime = MutableLiveData<String?>().apply {
        val time = history?.time ?: run {
            val timeMs = Calendar.getInstance().timeInMillis
            Converter.dateFormat(FORMAT_TIME_HOUR_MINUTE_WITH_MARKER, timeMs)
        }
        value = time
    }

    val isEmptyTime = editTime.map { it == null || it.isEmpty() }

    val editPrice = MutableLiveData<String?>().apply {
        val price = history?.transaction?.price
        value = price?.let { Converter.moneyFormat(price) }
    }

    val isEmptyPrice = editPrice.map { it == null || it.isEmpty() }

    val editDescription = MutableLiveData<String?>().apply {
        value = history?.transaction?.description
    }

    val isEmptyDescription = editDescription.map { it == null || it.isEmpty() }

    private val _inputEvent = MutableLiveData<Event<Pair<EditFilterType, String?>>>()
    val inputEvent: LiveData<Event<Pair<EditFilterType, String?>>> = _inputEvent

    private val _datePickerEvent = MutableLiveData<Event<String?>>()
    val datePickerEvent: LiveData<Event<String?>> = _datePickerEvent

    private val _timePickerEvent = MutableLiveData<Event<String?>>()
    val timePickerEvent: LiveData<Event<String?>> = _timePickerEvent

    private val _receiptEvent = MutableLiveData<Event<HistoryData>>()
    val receiptEvent: LiveData<Event<HistoryData>> = _receiptEvent

    private val paymentFilterType = MutableLiveData<Payment>().apply {
        value = history?.transaction?.payment?.let { Payment.find(it) }
    }

    val isSelectCash = paymentFilterType.map { it == Payment.CASH }
    val isSelectCard = paymentFilterType.map { it == Payment.CARD }

    private val _categoryFilterType = MutableLiveData<Category>()
        .apply { value = history?.transaction?.category?.let { Category.find(it) } }
    val categoryFilterType: LiveData<Category> = _categoryFilterType

    private var currentFilterType: EditFilterType? = null

    private var saveJob: Job? = null

    val usageTransactionTime = liveData { emit(getUsageTransactionTimeUseCase()) }

    fun edit(filterType: EditFilterType) {
        setFiltering(filterType)
        when {
            filterType.needToInputScreen() -> _inputEvent.value =
                Event(filterType to getContentByFilterType())
            filterType == EditFilterType.DATE -> _datePickerEvent.value = Event(editDate.value)
            filterType == EditFilterType.TIME -> _timePickerEvent.value = Event(editTime.value)
        }
    }

    fun payment(payment: Payment) {
        paymentFilterType.value = payment
    }

    fun category(category: Category) {
        _categoryFilterType.value = category
    }

    fun setFilter(text: String?) {
        when (currentFilterType) {
            EditFilterType.NAME -> editName
            EditFilterType.DATE -> editDate
            EditFilterType.TIME -> editTime
            EditFilterType.PRICE -> editPrice
            EditFilterType.DESCRIPTION -> editDescription
            null -> throw InvalidFilterTypeException()
        }.also {
            it.value = text
        }
    }

    fun register() {
        if (saveJob?.isActive == true) return
        saveJob = viewModelScope.launch {
            val user = history?.user ?: run {
                val result = getUserUseCase()
                suspendCoroutine {
                    if (result is Result.Success) {
                        it.resume(result.data)
                    }
                }
            }

            val date = Converter.dateParse(FORMAT_DATE_YEAR_DOT_MONTH_DOT_DAY, editDate.value)
            val time = Converter.dateParse(FORMAT_TIME_HOUR_MINUTE_WITH_MARKER, editTime.value)

            val category = categoryFilterType.value
            val payment = paymentFilterType.value

            val price = Converter.numberFormat(editPrice.value)?.takeIf { it > 0 }

            if (isEmptyName.value == true || isEmptyDate.value == true || isEmptyTime.value == true || price == null || category == null || payment == null) {
                toast(R.string.enter_all_items)
                return@launch
            }

            loading(true)

            val dateMs = Calendar.getInstance()
                .apply { this.time = time }
                .let {
                    Calendar.getInstance()
                        .apply { this.time = date }
                        .apply { set(Calendar.HOUR_OF_DAY, it.get(Calendar.HOUR_OF_DAY)) }
                        .apply { set(Calendar.MINUTE, it.get(Calendar.MINUTE)) }
                }
                .run { timeInMillis }

            val data = TransactionData(
                history?.transaction?.id ?: -1,
                user.id,
                user.code,
                editDescription.value,
                editName.value,
                category.ordinal,
                payment.ordinal,
                price,
                dateMs
            )

            if (data == history?.transaction) {
                loading(false)
                close()
            } else {
                val result = saveTransactionUseCase(data)
                loading(false)
                if (result.succeeded) {
                    val newHistory = HistoryData(
                        data,
                        user,
                        emptyList(),
                        true
                    )
                    receipt(newHistory)
                }
            }
        }
    }

    fun onDateSet(year: Int, month: Int, day: Int) {
        Calendar.getInstance()
            .apply { set(Calendar.YEAR, year) }
            .apply { set(Calendar.MONTH, month) }
            .apply { set(Calendar.DAY_OF_MONTH, day) }
            .run { timeInMillis }
            .let { Converter.dateFormat(FORMAT_DATE_YEAR_DOT_MONTH_DOT_DAY, it) }
            .let { editDate.value = it }
    }

    fun onTimeSet(hour: Int, minute: Int) {
        Calendar.getInstance()
            .apply { set(Calendar.HOUR_OF_DAY, hour) }
            .apply { set(Calendar.MINUTE, minute) }
            .run { timeInMillis }
            .let { Converter.dateFormat(FORMAT_TIME_HOUR_MINUTE_WITH_MARKER, it) }
            .let { editTime.value = it }
    }

    private fun setFiltering(filterType: EditFilterType) {
        currentFilterType = filterType
    }

    private fun getContentByFilterType(): String? {
        return when (currentFilterType) {
            EditFilterType.NAME -> editName.value
            EditFilterType.PRICE -> editPrice.value
            EditFilterType.DESCRIPTION -> editDescription.value
            else -> throw InvalidFilterTypeException()
        }
    }

    private fun receipt(history: HistoryData) {
        _receiptEvent.value = Event(history)
    }
}
