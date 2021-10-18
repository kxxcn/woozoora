package dev.kxxcn.woozoora.ui.direction.more

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import dev.kxxcn.woozoora.common.Event
import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.data.ifSucceeded
import dev.kxxcn.woozoora.domain.*
import dev.kxxcn.woozoora.domain.model.OptionData
import dev.kxxcn.woozoora.ui.base.BaseViewModel
import dev.kxxcn.woozoora.ui.edit.EditBranchType
import dev.kxxcn.woozoora.ui.policy.PolicyFilterType
import kotlinx.coroutines.launch
import javax.inject.Inject

class MoreViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getNotificationOptionUseCase: GetNotificationOptionUseCase,
    private val saveNotificationOptionUseCase: SaveNotificationOptionUseCase,
    private val getUsageTransactionTimeUseCase: GetUsageTransactionTimeUseCase,
    private val saveUsageTransactionTimeUseCase: SaveUsageTransactionTimeUseCase,
) : BaseViewModel() {

    private val _contactEvent = MutableLiveData<Event<Unit>>()
    val contactEvent: LiveData<Event<Unit>> = _contactEvent

    private val _codeEvent = MutableLiveData<Event<Unit>>()
    val codeEvent: LiveData<Event<Unit>> = _codeEvent

    private val _reviewEvent = MutableLiveData<Event<Unit>>()
    val reviewEvent: LiveData<Event<Unit>> = _reviewEvent

    private val _askEvent = MutableLiveData<Event<Unit>>()
    val askEvent: LiveData<Event<Unit>> = _askEvent

    private val _noticeEvent = MutableLiveData<Event<Unit>>()
    val noticeEvent: LiveData<Event<Unit>> = _noticeEvent

    private val _profileEvent = MutableLiveData<Event<Unit>>()
    val profileEvent: LiveData<Event<Unit>> = _profileEvent

    val user = liveData {
        val result = getUserUseCase()
        if (result is Result.Success) {
            emit(result.data)
        }
    }

    private val _notificationEvent = MutableLiveData<Event<OptionData>>()
    val notificationEvent: LiveData<Event<OptionData>> = _notificationEvent

    private val _policyEvent = MutableLiveData<Event<PolicyFilterType>>()
    val policyEvent: LiveData<Event<PolicyFilterType>> = _policyEvent

    private val _defaultNotification = MutableLiveData<Boolean>().apply {
        viewModelScope.launch {
            value = getNotificationOptionUseCase(OptionData.DEFAULT)
        }
    }
    val defaultNotification: LiveData<Boolean> = _defaultNotification

    private val _registrationNotification = MutableLiveData<Boolean>().apply {
        viewModelScope.launch {
            value = getNotificationOptionUseCase(OptionData.REGISTRATION)
        }
    }
    val registrationNotification: LiveData<Boolean> = _registrationNotification

    private val _dailyNotification = MutableLiveData<Boolean>().apply {
        viewModelScope.launch {
            value = getNotificationOptionUseCase(OptionData.DAILY)
        }
    }
    val dailyNotification: LiveData<Boolean> = _dailyNotification

    private val _weeklyNotification = MutableLiveData<Boolean>().apply {
        viewModelScope.launch {
            value = getNotificationOptionUseCase(OptionData.WEEKLY)
        }
    }
    val weeklyNotification: LiveData<Boolean> = _weeklyNotification

    private val _noticeNotification = MutableLiveData<Boolean>().apply {
        viewModelScope.launch {
            value = getNotificationOptionUseCase(OptionData.NOTICE)
        }
    }
    val noticeNotification: LiveData<Boolean> = _noticeNotification

    private val _usageTransactionTime = MutableLiveData<Boolean>().apply {
        viewModelScope.launch {
            value = getUsageTransactionTimeUseCase()
        }
    }
    val usageTransactionTime: LiveData<Boolean> = _usageTransactionTime

    private val currentDefaultFlag: Boolean
        get() = defaultNotification.value ?: false

    private val currentRegistrationFlag: Boolean
        get() = registrationNotification.value ?: false

    private val currentDailyFlag: Boolean
        get() = dailyNotification.value ?: false

    private val currentWeeklyFlag: Boolean
        get() = weeklyNotification.value ?: false

    private val currentNoticeFlag: Boolean
        get() = noticeNotification.value ?: false

    private val _sortEvent = MutableLiveData<Event<EditBranchType>>()
    val sortEvent: LiveData<Event<EditBranchType>> = _sortEvent

    fun contact() {
        _contactEvent.value = Event(Unit)
    }

    fun code() {
        _codeEvent.value = Event(Unit)
    }

    fun review() {
        _reviewEvent.value = Event(Unit)
    }

    fun ask() {
        _askEvent.value = Event(Unit)
    }

    fun notice() {
        _noticeEvent.value = Event(Unit)
    }

    fun profile() {
        _profileEvent.value = Event(Unit)
    }

    fun setNotification(requestType: OptionData) {
        _notificationEvent.value = Event(requestType)
    }

    fun policy(requestType: PolicyFilterType) {
        _policyEvent.value = Event(requestType)
    }

    fun setUsageTransactionTime() {
        _usageTransactionTime.value?.let { usage ->
            viewModelScope.launch {
                saveUsageTransactionTimeUseCase(!usage).ifSucceeded {
                    _usageTransactionTime.value = it
                }
            }
        }
    }

    fun saveNotificationOption(option: OptionData) {
        when (option) {
            OptionData.DEFAULT -> currentDefaultFlag
            OptionData.REGISTRATION -> currentRegistrationFlag
            OptionData.DAILY -> currentDailyFlag
            OptionData.WEEKLY -> currentWeeklyFlag
            OptionData.NOTICE -> currentNoticeFlag
        }.also {
            saveNotificationOption(option, !it)
        }
    }

    fun saveNotificationOption(values: List<Pair<String, Boolean>>) {
        values.forEach { (channel, value) ->
            OptionData.find(channel)?.let {
                saveNotificationOption(it, value)
            }
        }
    }

    fun sort(branch: EditBranchType) {
        _sortEvent.value = Event(branch)
    }

    private fun saveNotificationOption(option: OptionData, value: Boolean) {
        viewModelScope.launch {
            val result = saveNotificationOptionUseCase(option, value)
            if (result is Result.Success) {
                toggle(option, result.data)
            }
        }
    }

    private fun toggle(option: OptionData, value: Boolean) {
        when (option) {
            OptionData.DEFAULT -> _defaultNotification
            OptionData.REGISTRATION -> _registrationNotification
            OptionData.DAILY -> _dailyNotification
            OptionData.WEEKLY -> _weeklyNotification
            OptionData.NOTICE -> _noticeNotification
        }.also {
            it.value = value
        }
    }
}
