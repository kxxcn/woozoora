package dev.kxxcn.woozoora.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.woozoora.ui.sort.SortFragment
import dev.kxxcn.woozoora.ui.sort.SortViewModel

@Module
abstract class SortModule {

    @ContributesAndroidInjector(
        modules = [ViewModelBuilder::class]
    )
    internal abstract fun fragment(): SortFragment

    @Binds
    @IntoMap
    @ViewModelKey(SortViewModel::class)
    abstract fun bindViewModel(factory: SortViewModel.Factory): AssistedSavedStateViewModelFactory<out ViewModel>
}
