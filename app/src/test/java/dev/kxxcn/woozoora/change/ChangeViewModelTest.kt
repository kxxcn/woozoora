package dev.kxxcn.woozoora.change

import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.assertLiveDataEventTriggered
import dev.kxxcn.woozoora.base.BaseViewModelTest
import dev.kxxcn.woozoora.common.KEY_CHANGE_ITEM
import dev.kxxcn.woozoora.domain.UpdateUserUseCase
import dev.kxxcn.woozoora.domain.model.ChangeData
import dev.kxxcn.woozoora.ui.change.ChangeFilterType
import dev.kxxcn.woozoora.ui.change.ChangeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.junit.Test

@ExperimentalCoroutinesApi
@FlowPreview
class ChangeViewModelTest : BaseViewModelTest() {

    private lateinit var viewModel: ChangeViewModel

    @Test
    fun validateFilterType_year() {
        prepareViewModel(ChangeFilterType.YEAR)

        assertThat(viewModel.currentFilterType).isEqualTo(ChangeFilterType.YEAR)
    }

    @Test
    fun editYear_emptyValue() {
        prepareViewModel(ChangeFilterType.YEAR)

        viewModel.edit()

        assertLiveDataEventTriggered(viewModel.toastEvent, R.string.check_the_year_of_birth)
    }

    @Test
    fun editYear_invalidValue() {
        prepareViewModel(ChangeFilterType.YEAR)

        viewModel.yearText.value = "199"

        viewModel.edit()

        assertLiveDataEventTriggered(viewModel.toastEvent, R.string.check_the_year_of_birth)

        viewModel.yearText.value = "3000"

        viewModel.edit()

        assertLiveDataEventTriggered(viewModel.toastEvent, R.string.check_the_year_of_birth)

        viewModel.yearText.value = "1000"

        viewModel.edit()

        assertLiveDataEventTriggered(viewModel.toastEvent, R.string.try_again_after_a_while)
    }

    @Test
    fun editYear_showsSuccessMessage() {
        prepareViewModel(ChangeFilterType.YEAR)

        viewModel.yearText.value = "1990"

        viewModel.edit()

        assertLiveDataEventTriggered(viewModel.toastEvent, R.string.user_information_has_changed)
    }

    @Test
    fun validateFilterType_budget() {
        prepareViewModel(ChangeFilterType.BUDGET)

        assertThat(viewModel.currentFilterType).isEqualTo(ChangeFilterType.BUDGET)
    }

    @Test
    fun editBudget_emptyValue() {
        prepareViewModel(ChangeFilterType.BUDGET)

        viewModel.edit()

        assertLiveDataEventTriggered(viewModel.toastEvent, R.string.enter_your_budget)
    }

    @Test
    fun editBudget_invalidValue() {
        prepareViewModel(ChangeFilterType.BUDGET)

        viewModel.budgetText.value = "-1"

        viewModel.edit()

        assertLiveDataEventTriggered(viewModel.toastEvent, R.string.try_again_after_a_while)
    }

    @Test
    fun editBudget_showsSuccessMessage() {
        prepareViewModel(ChangeFilterType.BUDGET)

        viewModel.budgetText.value = "500000"

        viewModel.edit()

        assertLiveDataEventTriggered(viewModel.toastEvent, R.string.user_information_has_changed)
    }

    private fun prepareViewModel(filterType: ChangeFilterType) {
        viewModel = ChangeViewModel(
            UpdateUserUseCase(repository),
            SavedStateHandle().apply {
                set(KEY_CHANGE_ITEM, ChangeData("", filterType.ordinal))
            }
        )
    }
}
