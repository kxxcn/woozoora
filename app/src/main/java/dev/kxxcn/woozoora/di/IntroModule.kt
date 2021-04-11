package dev.kxxcn.woozoora.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.woozoora.ui.intro.IntroFragment
import dev.kxxcn.woozoora.ui.intro.IntroViewModel

@Module
abstract class IntroModule {

    @ContributesAndroidInjector(
        modules = [ViewModelBuilder::class]
    )
    internal abstract fun fragment(): IntroFragment

    @Binds
    @IntoMap
    @ViewModelKey(IntroViewModel::class)
    abstract fun bindViewModel(factory: IntroViewModel.Factory): AssistedSavedStateViewModelFactory<out ViewModel>
}
