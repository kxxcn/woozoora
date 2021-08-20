package dev.kxxcn.woozoora.ui.direction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import dev.kxxcn.woozoora.common.Event
import dev.kxxcn.woozoora.common.KEY_DEFAULT_PAGE
import dev.kxxcn.woozoora.common.KEY_INVITATION_ITEM
import dev.kxxcn.woozoora.di.AssistedSavedStateViewModelFactory
import dev.kxxcn.woozoora.domain.model.HistoryData
import dev.kxxcn.woozoora.domain.model.InvitationData
import dev.kxxcn.woozoora.domain.model.TimelineData
import dev.kxxcn.woozoora.ui.base.BaseViewModel
import dev.kxxcn.woozoora.ui.policy.PolicyFilterType

class DirectionViewModel @AssistedInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
) : BaseViewModel() {

    @AssistedInject.Factory
    interface Factory : AssistedSavedStateViewModelFactory<DirectionViewModel>

    val currentTabPosition: Int?
        get() = savedStateHandle.get<Int>(BOTTOM_NAVIGATOR_SAVED_STATE_KEY)

    private val _createEvent = MutableLiveData<Event<Unit>>()
    val createEvent: LiveData<Event<Unit>> = _createEvent

    private val _editEvent = MutableLiveData<Event<HistoryData?>>()
    val editEvent: LiveData<Event<HistoryData?>> = _editEvent

    private val _receiptEvent = MutableLiveData<Event<HistoryData>>()
    val receiptEvent: LiveData<Event<HistoryData>> = _receiptEvent

    private val _timelineEvent = MutableLiveData<Event<TimelineData>>()
    val timelineEvent: LiveData<Event<TimelineData>> = _timelineEvent

    private val _contactEvent = MutableLiveData<Event<Unit>>()
    val contactEvent: LiveData<Event<Unit>> = _contactEvent

    private val _codeEvent = MutableLiveData<Event<Unit>>()
    val codeEvent: LiveData<Event<Unit>> = _codeEvent

    private val _inviteEvent = MutableLiveData<Event<InvitationData>>()
    val inviteEvent: LiveData<Event<InvitationData>> = _inviteEvent

    private val _refreshEvent = MutableLiveData<Event<Unit>>()
    val refreshEvent: LiveData<Event<Unit>> = _refreshEvent

    private val _pageEvent = MutableLiveData<Event<Int>>()
    val pageEvent: LiveData<Event<Int>> = _pageEvent

    private val _policyEvent = MutableLiveData<Event<PolicyFilterType>>()
    val policyEvent: LiveData<Event<PolicyFilterType>> = _policyEvent

    private val _askEvent = MutableLiveData<Event<Unit>>()
    val askEvent: LiveData<Event<Unit>> = _askEvent

    private val _notificationEvent = MutableLiveData<Event<Unit>>()
    val notificationEvent: LiveData<Event<Unit>> = _notificationEvent

    private val _noticeEvent = MutableLiveData<Event<Unit>>()
    val noticeEvent: LiveData<Event<Unit>> = _noticeEvent

    private val _profileEvent = MutableLiveData<Event<Unit>>()
    val profileEvent: LiveData<Event<Unit>> = _profileEvent

    init {
        savedStateHandle.get<InvitationData>(KEY_INVITATION_ITEM)
            ?.takeIf { it.hasInvitation }
            ?.let { invite(it) }

        savedStateHandle.get<Int>(KEY_DEFAULT_PAGE)
            ?.let { _pageEvent.value = Event(it) }
    }

    fun create() {
        _createEvent.value = Event(Unit)
    }

    fun edit(history: HistoryData? = null) {
        _editEvent.value = Event(history)
    }

    fun saveInstanceState(position: Int) {
        savedStateHandle.set(BOTTOM_NAVIGATOR_SAVED_STATE_KEY, position)
    }

    fun receipt(history: HistoryData) {
        _receiptEvent.value = Event(history)
    }

    fun timeline(timeline: TimelineData) {
        _timelineEvent.value = Event(timeline)
    }

    fun contact() {
        _contactEvent.value = Event(Unit)
    }

    fun code() {
        _codeEvent.value = Event(Unit)
    }

    fun refresh() {
        _refreshEvent.value = Event(Unit)
    }

    fun policy(requestType: PolicyFilterType) {
        _policyEvent.value = Event(requestType)
    }

    fun ask() {
        _askEvent.value = Event(Unit)
    }

    fun notification() {
        _notificationEvent.value = Event(Unit)
    }

    fun notice() {
        _noticeEvent.value = Event(Unit)
    }

    fun profile() {
        _profileEvent.value = Event(Unit)
    }

    private fun invite(invitation: InvitationData) {
        _inviteEvent.value = Event(invitation)
    }
}

const val BOTTOM_NAVIGATOR_SAVED_STATE_KEY = "BOTTOM_NAVIGATOR_SAVED_STATE_KEY"
