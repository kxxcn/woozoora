package dev.kxxcn.woozoora.ui.policy

import androidx.lifecycle.SavedStateHandle
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import dev.kxxcn.woozoora.common.KEY_POLICY_TYPE
import dev.kxxcn.woozoora.di.AssistedSavedStateViewModelFactory
import dev.kxxcn.woozoora.ui.base.BaseViewModel

class PolicyViewModel @AssistedInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
) : BaseViewModel() {

    @AssistedInject.Factory
    interface Factory : AssistedSavedStateViewModelFactory<PolicyViewModel>

    val currentFilterType = savedStateHandle.get<PolicyFilterType>(KEY_POLICY_TYPE)
}
