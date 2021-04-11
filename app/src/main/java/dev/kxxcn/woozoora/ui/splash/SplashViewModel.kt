package dev.kxxcn.woozoora.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import dev.kxxcn.woozoora.common.Event
import dev.kxxcn.woozoora.common.KEY_USER_ID
import dev.kxxcn.woozoora.common.KEY_USER_NAME
import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.di.AssistedSavedStateViewModelFactory
import dev.kxxcn.woozoora.domain.GetUserUseCase
import dev.kxxcn.woozoora.domain.model.InvitationData
import dev.kxxcn.woozoora.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class SplashViewModel @AssistedInject constructor(
    private val getUserUseCase: GetUserUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle,
) : BaseViewModel() {

    @AssistedInject.Factory
    interface Factory : AssistedSavedStateViewModelFactory<SplashViewModel>

    private val _introEvent = MutableLiveData<Event<InvitationData>>()
    val introEvent: LiveData<Event<InvitationData>> = _introEvent

    private val _homeEvent = MutableLiveData<Event<InvitationData>>()
    val homeEvent: LiveData<Event<InvitationData>> = _homeEvent

    fun start() {
        val invitation = InvitationData(
            savedStateHandle.get<String>(KEY_USER_NAME),
            savedStateHandle.get<String>(KEY_USER_ID)
        )

        viewModelScope.launch {
            when (getUserUseCase()) {
                is Result.Success -> showHomeScreen(invitation)
                else -> showIntroScreen(invitation)
            }
        }
    }

    private fun showHomeScreen(invitation: InvitationData) {
        _homeEvent.value = Event(invitation)
    }

    private fun showIntroScreen(invitation: InvitationData) {
        _introEvent.value = Event(invitation)
    }
}
