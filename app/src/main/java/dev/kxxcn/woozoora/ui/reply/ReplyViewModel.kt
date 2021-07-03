package dev.kxxcn.woozoora.ui.reply

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import dev.kxxcn.woozoora.common.Event
import dev.kxxcn.woozoora.data.ifSucceeded
import dev.kxxcn.woozoora.domain.GetAsksUseCase
import dev.kxxcn.woozoora.ui.base.BaseViewModel
import javax.inject.Inject

class ReplyViewModel @Inject constructor(
    getAsksUseCase: GetAsksUseCase,
) : BaseViewModel() {

    val asks = liveData { getAsksUseCase().ifSucceeded { emit(it) } }

    private val _askEvent = MutableLiveData<Event<Unit>>()
    val askEvent: LiveData<Event<Unit>> = _askEvent

    fun ask() {
        _askEvent.value = Event(Unit)
    }
}
