package dev.kxxcn.woozoora.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.woozoora.ui.splash.SplashFragment
import dev.kxxcn.woozoora.ui.splash.SplashViewModel

@Module
abstract class SplashModule {

    @ContributesAndroidInjector(
        modules = [ViewModelBuilder::class]
    )
    internal abstract fun fragment(): SplashFragment

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindViewModel(factory: SplashViewModel.Factory): AssistedSavedStateViewModelFactory<out ViewModel>
}
