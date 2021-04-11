package dev.kxxcn.woozoora.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.woozoora.ui.change.ChangeFragment
import dev.kxxcn.woozoora.ui.change.ChangeViewModel

@Module
abstract class ChangeModule {

    @ContributesAndroidInjector(
        modules = [ViewModelBuilder::class]
    )
    internal abstract fun fragment(): ChangeFragment

    @Binds
    @IntoMap
    @ViewModelKey(ChangeViewModel::class)
    abstract fun bindViewModel(factory: ChangeViewModel.Factory): AssistedSavedStateViewModelFactory<out ViewModel>
}
