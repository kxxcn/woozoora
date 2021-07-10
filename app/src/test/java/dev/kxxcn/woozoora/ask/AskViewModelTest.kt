package dev.kxxcn.woozoora.ask

import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.assertLiveDataEventTriggered
import dev.kxxcn.woozoora.base.BaseViewModelTest
import dev.kxxcn.woozoora.common.TEST_USER_ID
import dev.kxxcn.woozoora.domain.SendAskUseCase
import dev.kxxcn.woozoora.domain.model.UserData
import dev.kxxcn.woozoora.ui.ask.AskViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
@FlowPreview
class AskViewModelTest : BaseViewModelTest() {

    private lateinit var viewModel: AskViewModel

    @Before
    fun setupViewModel() {
        viewModel = AskViewModel(
            SendAskUseCase(repository)
        )
    }

    @Test
    fun sendAsk_emptyMessage() {
        viewModel.send()

        assertLiveDataEventTriggered(viewModel.closeEvent, false)
    }

    @Test
    fun sendAsk_showsSuccessMessage() {
        repository.addUser(UserData(TEST_USER_ID))

        viewModel.editText.value = "문의사항입니다."

        viewModel.send()

        assertLiveDataEventTriggered(viewModel.loadEvent, true)

        mainCoroutineRule.resumeDispatcher()

        assertLiveDataEventTriggered(viewModel.loadEvent, false)

        assertLiveDataEventTriggered(
            viewModel.toastEvent,
            R.string.thank_you_for_your_valuable_feedback
        )
    }

    @Test
    fun sendAsk_showsFailureMessage() {
        repository.clearUser()

        viewModel.editText.value = "문의사항입니다."

        viewModel.send()

        assertLiveDataEventTriggered(viewModel.loadEvent, true)

        mainCoroutineRule.resumeDispatcher()

        assertLiveDataEventTriggered(viewModel.loadEvent, false)

        assertLiveDataEventTriggered(
            viewModel.toastEvent,
            R.string.try_again_after_a_while
        )
    }
}

