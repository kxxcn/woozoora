package dev.kxxcn.woozoora.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import dev.kxxcn.woozoora.MainCoroutineRule
import dev.kxxcn.woozoora.assertLiveDataEventTriggered
import dev.kxxcn.woozoora.common.TEST_USER_ID
import dev.kxxcn.woozoora.data.source.FakeRepository
import dev.kxxcn.woozoora.domain.GetUserUseCase
import dev.kxxcn.woozoora.domain.model.InvitationData
import dev.kxxcn.woozoora.domain.model.UserData
import dev.kxxcn.woozoora.ui.splash.SplashViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SplashViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: FakeRepository

    private lateinit var viewModel: SplashViewModel

    @Before
    fun setupViewModel() {
        repository = FakeRepository()

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
