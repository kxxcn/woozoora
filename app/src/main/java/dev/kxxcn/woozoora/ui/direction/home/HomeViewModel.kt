package dev.kxxcn.woozoora.ui.direction.home

import androidx.lifecycle.*
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import dev.kxxcn.woozoora.common.Event
import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.di.AssistedSavedStateViewModelFactory
import dev.kxxcn.woozoora.domain.GetNotificationsUseCase
import dev.kxxcn.woozoora.domain.GetOverviewUseCase
import dev.kxxcn.woozoora.domain.model.OverviewData
import dev.kxxcn.woozoora.ui.base.MotionViewModel

class HomeViewModel @AssistedInject constructor(
    getNotificationsUseCase: GetNotificationsUseCase,
    private val getOverviewUseCase: GetOverviewUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle,
) : MotionViewModel(savedStateHandle) {

    @AssistedInject.Factory
    interface Factory : AssistedSavedStateViewModelFactory<HomeViewModel>

    private val forceObserve = MutableLiveData<Boolean>()

    val hasNewNotification = forceObserve.switchMap { force ->
        if (force) {
            getNotificationsUseCase().map { notifications -> notifications.any { !it.isChecked } }
        } else {
            MutableLiveData<Boolean>().apply { value = force }
        }
    }

    private val _filterType =
        MutableLiveData<HomeFilterType>().apply { value = HomeFilterType.MONTHLY }
    val filterType: LiveData<HomeFilterType> = _filterType

    private val forceUpdate = MutableLiveData<Unit>()

    private val _isProgress = MutableLiveData<Event<Boolean>>()
    val isProgress: LiveData<Event<Boolean>> = _isProgress

    private val _contactEvent = MutableLiveData<Event<Unit>>()
    val contactEvent: LiveData<Event<Unit>> = _contactEvent

    private val _notificationEvent = MutableLiveData<Event<Unit>>()
    val notificationEvent: LiveData<Event<Unit>> = _notificationEvent

    val overview = forceUpdate.switchMap {
        liveData {
            progress(true)
            val result = getOverviewUseCase()
            if (result is Result.Success) {
                if (isUpdatable(result.data)) {
                    emit(result.data)
                }
                progress(false)
            }
        }
    }

    val userProfile = overview.map { it.profile }
    val userName = overview.map { it.name }

    private val currentOverview: OverviewData?
        get() = overview.value

    fun start() {
        forceUpdate.value = Unit
    }

    fun observe(force: Boolean) {
        forceObserve.value = force
    }

    fun contact() {
        _contactEvent.value = Event(Unit)
    }

    fun notification() {
        _notificationEvent.value = Event(Unit)
    }

    private fun isUpdatable(overview: OverviewData): Boolean {
        return overview.transactions != currentOverview?.transactions || overview.user != currentOverview?.user
    }

    private fun progress(isLoading: Boolean) {
        _isProgress.value = Event(isLoading)
    }
}
