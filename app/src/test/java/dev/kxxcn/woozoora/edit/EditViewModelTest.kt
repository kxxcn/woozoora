package dev.kxxcn.woozoora.edit

import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import dev.kxxcn.woozoora.LiveDataTestUtil
import dev.kxxcn.woozoora.base.BaseViewModelTest
import dev.kxxcn.woozoora.common.KEY_BRANCH_TYPE
import dev.kxxcn.woozoora.domain.*
import dev.kxxcn.woozoora.domain.model.TransactionCategoryData
import dev.kxxcn.woozoora.getOrAwaitValue
import dev.kxxcn.woozoora.ui.edit.EditBranchType
import dev.kxxcn.woozoora.ui.edit.EditViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.junit.Before
import org.junit.Test

@FlowPreview
@ExperimentalCoroutinesApi
class EditViewModelTest : BaseViewModelTest() {


    private lateinit var viewModel: EditViewModel

    @Before
    fun setupViewModel() {
        val category1 = TransactionCategoryData(0, "식비", 0)
        val category2 = TransactionCategoryData(1, "교통", 1)
        val category3 = TransactionCategoryData(2, "문화생활", 2)
        repository.addTransactionCategories(category1, category2, category3)

        viewModel = EditViewModel(
            GetUserUseCase(repository),
            SaveTransactionUseCase(repository),
            SaveNotificationUseCase(repository),
            GetUsageTransactionTimeUseCase(repository),
            GetAssetCategoryUseCase(repository),
            GetTransactionCategoryUseCase(repository),
            SavedStateHandle().apply { set(KEY_BRANCH_TYPE, EditBranchType.TRANSACTION) },
        )
    }

    @Test
    fun loadCategoryFromRepository() {
        assertThat(LiveDataTestUtil.getValue(viewModel.category)).hasSize(3)
    }

    @Test
    fun toggleCategory_showSelectCategoryUi() {
        val checkedId = viewModel.category.getOrAwaitValue().first().id

        viewModel.category(checkedId)

        val value = viewModel.category.getOrAwaitValue()

        val selectedCategory = value.find { it.isSelected }

        assertThat(selectedCategory?.id).isEqualTo(checkedId)
    }
}
