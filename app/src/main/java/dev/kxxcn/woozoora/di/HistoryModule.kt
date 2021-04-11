package dev.kxxcn.woozoora.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.woozoora.ui.direction.history.HistoryFragment
import dev.kxxcn.woozoora.ui.direction.history.HistoryViewModel

@Module
abstract class HistoryModule {

    @ContributesAndroidInjector(
        modules = [ViewModelBuilder::class]
    )
    internal abstract fun fragment(): HistoryFragment

    @Binds
    @IntoMap
    @ViewModelKey(HistoryViewModel::class)
    abstract fun bindViewModel(factory: HistoryViewModel.Factory): AssistedSavedStateViewModelFactory<out ViewModel>
}
