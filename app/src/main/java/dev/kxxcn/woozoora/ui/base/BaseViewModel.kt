package dev.kxxcn.woozoora.ui.base

import androidx.lifecycle.*
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.DURATION_TWO_SECONDS
import dev.kxxcn.woozoora.common.Event
import dev.kxxcn.woozoora.common.KEY_DEFAULT_DATE
import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.domain.model.DatePickerData
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

open class BaseViewModel(
    private val savedStateHandle: SavedStateHandle? = null,
) : ViewModel() {

    private val _closeEvent = MutableLiveData<Event<Boolean>>()
    val closeEvent: LiveData<Event<Boolean>> = _closeEvent

    private val _loadEvent = MutableLiveData<Event<Boolean>>()
    val loadEvent: LiveData<Event<Boolean>> = _loadEvent

    private val _animationEvent = MutableLiveData<Event<String>>()
    val animationEvent: LiveData<Event<String>> = _animationEvent

    private val _hideEvent = MutableLiveData<Event<Unit>>()
    val hideEvent: LiveData<Event<Unit>> = _hideEvent

    private val _toastEvent = MutableLiveData<Event<Any>>()
    val toastEvent: LiveData<Event<Any>> = _toastEvent

    private val _dateEvent = MutableLiveData<Event<DatePickerData>>()
    val dateEvent: LiveData<Event<DatePickerData>> = _dateEvent

    private val _analyticsEvent = MutableLiveData<Event<Int>>()
    val analyticsEvent: LiveData<Event<Int>> = _analyticsEvent

    private val _finishEvent = MutableLiveData<Event<Unit>>()
    val finishEvent: LiveData<Event<Unit>> = _finishEvent

    val dateTimeMs = MutableLiveData<Long>().apply {
        value = savedStateHandle?.get<Long>(KEY_DEFAULT_DATE) ?: Calendar.getInstance().timeInMillis
    }

    private val channel = BroadcastChannel<Long>(Channel.BUFFERED)

    private var backPressedTimeMs = 0L

    init {
        observeBackPressed()
    }

    fun toastAndClose(any: Any?, forceStop: Boolean) {
        toast(any)
        close(forceStop)
    }

    @JvmOverloads
    fun close(forceStop: Boolean = false) {
        _closeEvent.value = Event(forceStop)
    }

    fun hideAndClose() {
        close()
        hideKeyboard()
    }

    fun hideKeyboard() {
        _hideEvent.value = Event(Unit)
    }

    fun loading(isVisible: Boolean) {
        _loadEvent.value = Event(isVisible)
    }

    fun loading(fileName: String) {
        _animationEvent.value = Event(fileName)
    }

    fun toast(any: Any?) {
        when (any) {
            is Int -> _toastEvent.value = Event(any)
            is String -> _toastEvent.value = Event(any)
            is Result.Error -> {
                val message = any.messageRes ?: R.string.try_again_after_a_while
                _toastEvent.value = Event(message)
            }
        }
    }

    fun date(isToday: Boolean) {
        dateTimeMs.value?.let {
            val nameRes = if (isToday) R.string.today else R.string.this_month
            val datePicker = DatePickerData(nameRes, it)
            _dateEvent.value = Event(datePicker)
        }
    }

    fun selectDate(timeMs: Long?) {
        timeMs?.let { dateTimeMs.value = it }
    }

    fun analytics(nameRes: Int) {
        _analyticsEvent.value = Event(nameRes)
    }

    fun onBackPressed() {
        viewModelScope.launch {
            channel.send(System.currentTimeMillis())
        }
    }

    fun finish() {
        _finishEvent.value = Event(Unit)
    }

    private fun observeBackPressed() {
        viewModelScope.launch {
            channel
                .asFlow()
                .collect {
                    if (backPressedTimeMs == 0L) {
                        backPressedTimeMs = it
                        toast(R.string.close_app_press_back_button_more)
                        return@collect
                    } else {
                        if (it - backPressedTimeMs < DURATION_TWO_SECONDS) {
                            finish()
                        } else {
                            backPressedTimeMs = 0L
                            onBackPressed()
                        }
                    }
                }
        }
    }

}
