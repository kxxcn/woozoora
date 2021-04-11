package dev.kxxcn.woozoora.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.woozoora.ui.timeline.TimelineFragment
import dev.kxxcn.woozoora.ui.timeline.TimelineViewModel

@Module
abstract class TimelineModule {

    @ContributesAndroidInjector(
        modules = [ViewModelBuilder::class]
    )
    internal abstract fun fragment(): TimelineFragment

    @Binds
    @IntoMap
    @ViewModelKey(TimelineViewModel::class)
    abstract fun bindViewModel(factory: TimelineViewModel.Factory): AssistedSavedStateViewModelFactory<out ViewModel>
}
