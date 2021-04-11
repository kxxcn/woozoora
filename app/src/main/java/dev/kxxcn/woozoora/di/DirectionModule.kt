package dev.kxxcn.woozoora.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.woozoora.ui.direction.DirectionFragment
import dev.kxxcn.woozoora.ui.direction.DirectionViewModel

@Module
abstract class DirectionModule {

    @ContributesAndroidInjector(
        modules = [ViewModelBuilder::class]
    )
    internal abstract fun fragment(): DirectionFragment

    @Binds
    @IntoMap
    @ViewModelKey(DirectionViewModel::class)
    abstract fun bindViewModel(factory: DirectionViewModel.Factory): AssistedSavedStateViewModelFactory<out ViewModel>
}
