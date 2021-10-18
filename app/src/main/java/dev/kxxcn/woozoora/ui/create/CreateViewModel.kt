package dev.kxxcn.woozoora.ui.create

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.InvalidFilterTypeException
import dev.kxxcn.woozoora.common.KEY_BRANCH_TYPE
import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.di.AssistedSavedStateViewModelFactory
import dev.kxxcn.woozoora.domain.SaveAssetCategoryUseCase
import dev.kxxcn.woozoora.domain.SaveTransactionCategoryUseCase
import dev.kxxcn.woozoora.ui.base.BaseViewModel
import dev.kxxcn.woozoora.ui.edit.EditBranchType
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CreateViewModel @AssistedInject constructor(
    private val saveAssetCategoryUseCase: SaveAssetCategoryUseCase,
    private val saveTransactionCategoryUseCase: SaveTransactionCategoryUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle,
) : BaseViewModel() {

    @AssistedInject.Factory
    interface Factory : AssistedSavedStateViewModelFactory<CreateViewModel>

    private val currentFilterType = savedStateHandle
        .get<EditBranchType>(KEY_BRANCH_TYPE)
        ?: throw InvalidFilterTypeException()

    val editText = MutableLiveData<String>()

    private var saveJob: Job? = null

    fun create() {
        if (saveJob?.isActive == true) return
        editText.value
            ?.takeIf { it.isNotEmpty() }
            ?.let {
                saveJob = viewModelScope.launch {
                    val result = when (currentFilterType) {
                        EditBranchType.TRANSACTION -> saveTransactionCategoryUseCase(it)
                        EditBranchType.ASSET -> saveAssetCategoryUseCase(it)
                    }
                    if (result is Result.Success) {
                        toastWithArgs(R.string.category_has_been_added, it)
                        close()
                    } else if (result is Result.Error) {
                        toast(result)
                    }
                }
            } ?: close()
    }
}
