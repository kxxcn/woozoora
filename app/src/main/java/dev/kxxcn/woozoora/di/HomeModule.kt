package dev.kxxcn.woozoora.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.woozoora.ui.direction.home.HomeFragment
import dev.kxxcn.woozoora.ui.direction.home.HomeViewModel

@Module
abstract class HomeModule {

    @ContributesAndroidInjector(
        modules = [ViewModelBuilder::class]
    )
    internal abstract fun fragment(): HomeFragment

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindViewModel(factory: HomeViewModel.Factory): AssistedSavedStateViewModelFactory<out ViewModel>
}
