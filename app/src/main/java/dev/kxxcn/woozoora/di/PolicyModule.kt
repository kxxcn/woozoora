package dev.kxxcn.woozoora.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.woozoora.ui.policy.PolicyFragment
import dev.kxxcn.woozoora.ui.policy.PolicyViewModel

@Module
abstract class PolicyModule {

    @ContributesAndroidInjector(
        modules = [ViewModelBuilder::class]
    )
    internal abstract fun fragment(): PolicyFragment

    @Binds
    @IntoMap
    @ViewModelKey(PolicyViewModel::class)
    abstract fun bindViewModel(factory: PolicyViewModel.Factory): AssistedSavedStateViewModelFactory<out ViewModel>
}
