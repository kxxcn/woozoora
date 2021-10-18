package dev.kxxcn.woozoora.ui.profile

import androidx.lifecycle.*
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.Event
import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.data.ifFailed
import dev.kxxcn.woozoora.data.ifSucceeded
import dev.kxxcn.woozoora.domain.GetUserUseCase
import dev.kxxcn.woozoora.domain.LeaveUseCase
import dev.kxxcn.woozoora.domain.model.ChangeData
import dev.kxxcn.woozoora.ui.base.BaseViewModel
import dev.kxxcn.woozoora.ui.profile.item.ProfileItem
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    getUserUseCase: GetUserUseCase,
    private val leaveUseCase: LeaveUseCase,
) : BaseViewModel() {

    private val forceUpdate = MutableLiveData<Unit>()

    val user = forceUpdate.switchMap {
        liveData {
            val result = getUserUseCase()
            if (result is Result.Success) {
                emit(result.data)
            }
        }
    }

    private val _changeEvent = MutableLiveData<Event<ChangeData>>()
    val changeEvent: LiveData<Event<ChangeData>> = _changeEvent

    private val _leaveEvent = MutableLiveData<Event<Unit>>()
    val leaveEvent: LiveData<Event<Unit>> = _leaveEvent

    fun start() {
        forceUpdate.value = Unit
    }

    fun change(item: ProfileItem) {
        val data = ChangeData(item.content, item.filterType?.ordinal)
        _changeEvent.value = Event(data)
    }

    fun leaveDialog() {
        _leaveEvent.value = Event(Unit)
    }

    fun leave() {
        viewModelScope.launch {
            leaveUseCase()
                .ifSucceeded { toastAndClose(R.string.thank_you_for_using, true) }
                .ifFailed { toastAndClose(it.messageRes) }
        }
    }
}
