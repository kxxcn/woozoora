package dev.kxxcn.woozoora.splash

import androidx.lifecycle.SavedStateHandle
import dev.kxxcn.woozoora.assertLiveDataEventTriggered
import dev.kxxcn.woozoora.base.BaseViewModelTest
import dev.kxxcn.woozoora.common.TEST_USER_ID
import dev.kxxcn.woozoora.domain.GetUserUseCase
import dev.kxxcn.woozoora.domain.model.InvitationData
import dev.kxxcn.woozoora.domain.model.UserData
import dev.kxxcn.woozoora.ui.splash.SplashViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
@FlowPreview
class SplashViewModelTest : BaseViewModelTest() {

    private lateinit var viewModel: SplashViewModel

    @Before
    fun setupViewModel() {
        viewModel = SplashViewModel(
            GetUserUseCase(repository),
            SavedStateHandle()
        )
    }

    @Test
    fun activeUser() {
        repository.addUser(UserData(TEST_USER_ID))

        viewModel.start()

        assertLiveDataEventTriggered(viewModel.homeEvent, InvitationData())
    }

    @Test
    fun inactiveUser() {
        repository.clearUser()

        viewModel.start()

        assertLiveDataEventTriggered(viewModel.introEvent, InvitationData())
    }
}
