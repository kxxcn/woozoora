package dev.kxxcn.woozoora.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.woozoora.ui.create.CreateFragment
import dev.kxxcn.woozoora.ui.create.CreateViewModel

@Module
abstract class CreateModule {

    @ContributesAndroidInjector(
        modules = [ViewModelBuilder::class]
    )
    internal abstract fun fragment(): CreateFragment

    @Binds
    @IntoMap
    @ViewModelKey(CreateViewModel::class)
    abstract fun bindViewModel(factory: CreateViewModel.Factory): AssistedSavedStateViewModelFactory<out ViewModel>
}