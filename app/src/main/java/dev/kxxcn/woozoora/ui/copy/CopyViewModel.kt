package dev.kxxcn.woozoora.ui.copy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.Event
import dev.kxxcn.woozoora.data.ifSucceeded
import dev.kxxcn.woozoora.domain.GetUserUseCase
import dev.kxxcn.woozoora.ui.base.BaseViewModel
import javax.inject.Inject

class CopyViewModel @Inject constructor(
    getUserUseCase: GetUserUseCase,
) : BaseViewModel() {

    val code = liveData { getUserUseCase().ifSucceeded { emit(it.code) } }

    private val _copyEvent = MutableLiveData<Event<String?>>()
    val copyEvent: LiveData<Event<String?>> = _copyEvent

    fun copy() {
        copy(code.value)
        toast(R.string.copied)
    }

    private fun copy(code: String?) {
        _copyEvent.value = Event(code)
    }
}
