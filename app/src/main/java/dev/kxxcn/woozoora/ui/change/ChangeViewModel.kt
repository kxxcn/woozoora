package dev.kxxcn.woozoora.ui.change

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.KEY_CHANGE_ITEM
import dev.kxxcn.woozoora.common.extension.year
import dev.kxxcn.woozoora.data.succeeded
import dev.kxxcn.woozoora.di.AssistedSavedStateViewModelFactory
import dev.kxxcn.woozoora.domain.UpdateUserUseCase
import dev.kxxcn.woozoora.domain.model.ChangeData
import dev.kxxcn.woozoora.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import java.util.*

class ChangeViewModel @AssistedInject constructor(
    private val updateUserUseCase: UpdateUserUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle,
) : BaseViewModel() {

    @AssistedInject.Factory
    interface Factory : AssistedSavedStateViewModelFactory<ChangeViewModel>

    val change = savedStateHandle.get<ChangeData>(KEY_CHANGE_ITEM)

    val currentFilterType = ChangeFilterType.find(change?.ordinal)

    val nameRes = currentFilterType?.nameRes

    val budgetText = MutableLiveData<String>()

    val yearText = MutableLiveData<String>()

    fun edit() {
        when (currentFilterType) {
            ChangeFilterType.YEAR -> updateYear()
            ChangeFilterType.BUDGET -> updateBudget()
            else -> {}
        }
    }

    private fun updateYear() {
        yearText.value
            .takeIf { it?.length == 4 && it.toInt() <= Calendar.getInstance().year }
            ?.let {
                viewModelScope.launch {
                    val result = updateUserUseCase(it.toInt())
                    if (result.succeeded) {
                        toast(R.string.user_information_has_changed)
                    } else {
                        toast(R.string.try_again_after_a_while)
                    }
                    hideAndClose()
                }
            } ?: toast(R.string.check_the_year_of_birth)
    }

    private fun updateBudget() {
        budgetText.value
            .takeIf { it?.length != 0 }
            ?.let {
                viewModelScope.launch {
                    val result = updateUserUseCase(it.toLong())
                    if (result.succeeded) {
                        toast(R.string.user_information_has_changed)
                    } else {
                        toast(R.string.try_again_after_a_while)
                    }
                    hideAndClose()
                }
            } ?: toast(R.string.enter_your_budget)
    }
}
