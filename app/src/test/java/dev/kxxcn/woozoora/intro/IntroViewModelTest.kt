package dev.kxxcn.woozoora.intro

import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.assertLiveDataEventTriggered
import dev.kxxcn.woozoora.base.BaseViewModelTest
import dev.kxxcn.woozoora.common.TEST_USER_BUDGET
import dev.kxxcn.woozoora.common.TEST_USER_ID
import dev.kxxcn.woozoora.common.TEST_USER_YEAR
import dev.kxxcn.woozoora.domain.GetUserUseCase
import dev.kxxcn.woozoora.domain.SaveUserUseCase
import dev.kxxcn.woozoora.domain.UpdateTokenUseCase
import dev.kxxcn.woozoora.domain.model.AccountData
import dev.kxxcn.woozoora.domain.model.UserData
import dev.kxxcn.woozoora.ui.intro.IntroFilterType
import dev.kxxcn.woozoora.ui.intro.IntroViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
@FlowPreview
class IntroViewModelTest : BaseViewModelTest() {

    private lateinit var viewModel: IntroViewModel

    @Before
    fun setupViewModel() {
        viewModel = IntroViewModel(
            GetUserUseCase(repository),
            UpdateTokenUseCase(repository),
            SaveUserUseCase(repository),
            SavedStateHandle()
        )
    }

    @Test
    fun invalidSignInIfFilterTypeIsNull() {
        viewModel.signIn()

        assertLiveDataEventTriggered(viewModel.toastEvent, R.string.select_a_sign_in_method)
    }

    @Test
    fun selectKakaoButton() {
        viewModel.select(R.id.scene_kakao)

        assertThat(viewModel.filterType).isEqualTo(IntroFilterType.KAKAO)

        viewModel.signIn()

        assertLiveDataEventTriggered(viewModel.kakaoEvent, Unit)
    }

    @Test
    fun selectGoogleButton() {
        viewModel.select(R.id.scene_google)

        assertThat(viewModel.filterType).isEqualTo(IntroFilterType.GOOGLE)

        viewModel.signIn()

        assertLiveDataEventTriggered(viewModel.googleEvent, Unit)
    }

    @Test
    fun showHomeScreenIfAlreadyExist() {
        val account = AccountData(TEST_USER_ID)

        account.id?.let {
            repository.addUser(UserData(it))

            viewModel.validateUser(account)

            assertThat(viewModel.account).isNotNull()

            assertLiveDataEventTriggered(viewModel.homeEvent, Unit)
        }
    }

    @Test
    fun nextEventIfValidBudgetValue() {
        viewModel.budgetEdit.value = TEST_USER_BUDGET

        viewModel.next()

        assertLiveDataEventTriggered(viewModel.nextEvent, R.id.transition_year_from_budget)
    }

    @Test
    fun invalidBudgetValue() {
        viewModel.next()

        assertLiveDataEventTriggered(viewModel.toastEvent, R.string.enter_your_budget)
    }

    @Test
    fun registerIfValidYearValue() {
        val account = AccountData(TEST_USER_ID)

        viewModel.validateUser(account)

        viewModel.yearEdit.value = TEST_USER_YEAR

        viewModel.start()

        assertLiveDataEventTriggered(viewModel.homeEvent, Unit)
    }

    @Test
    fun invalidYearValue() {
        viewModel.start()

        assertLiveDataEventTriggered(viewModel.toastEvent, R.string.check_the_year_of_birth)
    }
}
