package dev.kxxcn.woozoora.contact

import com.google.common.truth.Truth.assertThat
import dev.kxxcn.woozoora.LiveDataTestUtil
import dev.kxxcn.woozoora.assertLiveDataEventTriggered
import dev.kxxcn.woozoora.base.BaseViewModelTest
import dev.kxxcn.woozoora.common.TEST_USER_ID
import dev.kxxcn.woozoora.domain.GetUserUseCase
import dev.kxxcn.woozoora.domain.model.UserData
import dev.kxxcn.woozoora.ui.contact.ContactViewModel
import dev.kxxcn.woozoora.ui.contact.item.ContactItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.junit.Before
import org.junit.Test

@FlowPreview
@ExperimentalCoroutinesApi
class ContactViewModelTest : BaseViewModelTest() {

    private lateinit var viewModel: ContactViewModel

    @Before
    fun setupViewModel() {
        viewModel = ContactViewModel(
            GetUserUseCase(repository)
        )
    }

    @Test
    fun loadContactsFromDevice() {
        val contact1 = ContactItem("Contact 1", "010-1234-5678")
        val contact2 = ContactItem("Contact 2", "010-3377-6688")
        val contact3 = ContactItem("Contact 3", "010-2323-0707")
        val contacts = listOf(contact1, contact2, contact3)

        viewModel.contact(contacts)

        assertThat(LiveDataTestUtil.getValue(viewModel.contacts)).hasSize(3)
    }

    @Test
    fun inviteFromSelectedUser() {
        val user = UserData(TEST_USER_ID)

        repository.addUser(user)

        val contact = ContactItem("Contact 1", "010-1234-5678")

        viewModel.invite(contact)

        assertLiveDataEventTriggered(viewModel.inviteEvent, contact to user)
    }
}
