package dev.kxxcn.woozoora.ui.invite

import androidx.lifecycle.*
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import dev.kxxcn.woozoora.common.Event
import dev.kxxcn.woozoora.common.KEY_INVITATION_ITEM
import dev.kxxcn.woozoora.data.succeeded
import dev.kxxcn.woozoora.di.AssistedSavedStateViewModelFactory
import dev.kxxcn.woozoora.domain.UpdateUserUseCase
import dev.kxxcn.woozoora.domain.model.InvitationData
import kotlinx.coroutines.launch

class InviteViewModel @AssistedInject constructor(
    private val updateUserUseCase: UpdateUserUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    @AssistedInject.Factory
    interface Factory : AssistedSavedStateViewModelFactory<InviteViewModel>

    val invitation = savedStateHandle.get<InvitationData>(KEY_INVITATION_ITEM)

    private val _closeEvent = MutableLiveData<Event<Unit>>()
    val closeEvent: LiveData<Event<Unit>> = _closeEvent

    private val _completeEvent = MutableLiveData<Event<Unit>>()
    val completeEvent: LiveData<Event<Unit>> = _completeEvent

    private val _isAgree = MutableLiveData<Boolean>().apply { value = true }
    val isAgree: LiveData<Boolean> = _isAgree

    private val currentAgreeFlag: Boolean
        get() = isAgree.value ?: true

    fun close() {
        _closeEvent.value = Event(Unit)
    }

    fun toggle() {
        _isAgree.value = !currentAgreeFlag
    }

    fun together() {
        viewModelScope.launch {
            invitation?.id?.let {
                val result = updateUserUseCase(it, currentAgreeFlag)
                if (result.succeeded) {
                    _completeEvent.value = Event(Unit)
                }
            }
        }
    }
}
