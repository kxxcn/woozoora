package dev.kxxcn.woozoora.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.woozoora.ui.edit.EditFragment
import dev.kxxcn.woozoora.ui.edit.EditViewModel

@Module
abstract class EditModule {

    @ContributesAndroidInjector(
        modules = [ViewModelBuilder::class]
    )
    internal abstract fun fragment(): EditFragment

    @Binds
    @IntoMap
    @ViewModelKey(EditViewModel::class)
    abstract fun bindViewModel(factory: EditViewModel.Factory): AssistedSavedStateViewModelFactory<out ViewModel>
}
