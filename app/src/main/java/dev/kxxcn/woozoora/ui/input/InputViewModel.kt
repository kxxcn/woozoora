package dev.kxxcn.woozoora.ui.input

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.kxxcn.woozoora.common.Event
import dev.kxxcn.woozoora.ui.base.BaseViewModel
import dev.kxxcn.woozoora.ui.edit.EditFilterType

class InputViewModel : BaseViewModel() {

    val editText = MutableLiveData<String>()

    val priceText = MutableLiveData<String>()

    private val _inputEvent = MutableLiveData<Event<String>>()
    val inputEvent: LiveData<Event<String>> = _inputEvent

    private val _currentFilterType = MutableLiveData<EditFilterType>()
    val currentFilterType: LiveData<EditFilterType> = _currentFilterType

    fun start(requestType: EditFilterType?) {
        forceClear()
        setFiltering(requestType)
    }

    fun emitAndClose() {
        emit()
        hideAndClose()
    }

    private fun forceClear() {
        editText.value = null
        priceText.value = null
    }

    private fun setFiltering(requestType: EditFilterType?) {
        _currentFilterType.value = requestType
    }

    private fun emit() {
        when (currentFilterType.value) {
            EditFilterType.PRICE -> priceText.value
            else -> editText.value
        }.also { value ->
            value?.takeIf { it.isNotEmpty() }?.let { _inputEvent.value = Event(it) }
        }
    }
}
