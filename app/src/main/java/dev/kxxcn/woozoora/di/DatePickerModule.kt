package dev.kxxcn.woozoora.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.woozoora.ui.date.DatePickerFragment
import dev.kxxcn.woozoora.ui.date.DatePickerViewModel

@Module
abstract class DatePickerModule {

    @ContributesAndroidInjector(
        modules = [ViewModelBuilder::class]
    )
    internal abstract fun fragment(): DatePickerFragment

    @Binds
    @IntoMap
    @ViewModelKey(DatePickerViewModel::class)
    abstract fun bindViewModel(factory: DatePickerViewModel.Factory): AssistedSavedStateViewModelFactory<out ViewModel>
}
