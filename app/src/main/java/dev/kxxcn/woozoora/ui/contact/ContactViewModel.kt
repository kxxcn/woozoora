package dev.kxxcn.woozoora.ui.contact

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.kxxcn.woozoora.common.Event
import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.domain.GetUserUseCase
import dev.kxxcn.woozoora.domain.model.UserData
import dev.kxxcn.woozoora.ui.base.BaseViewModel
import dev.kxxcn.woozoora.ui.contact.item.ContactItem
import kotlinx.coroutines.launch
import javax.inject.Inject

class ContactViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase
) : BaseViewModel() {

    private val _contacts = MutableLiveData<List<ContactItem>>()
    val contacts: LiveData<List<ContactItem>> = _contacts

    private val _inviteEvent = MutableLiveData<Event<Pair<ContactItem, UserData>>>()
    val inviteEvent: LiveData<Event<Pair<ContactItem, UserData>>> = _inviteEvent

    fun contact(contacts: List<ContactItem>) {
        _contacts.value = contacts
    }

    fun invite(contact: ContactItem) {
        viewModelScope.launch {
            val result = getUserUseCase()
            if (result is Result.Success) {
                _inviteEvent.value = Event(contact to result.data)
            }
        }
    }
}
