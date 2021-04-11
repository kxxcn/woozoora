package dev.kxxcn.woozoora.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.woozoora.ui.direction.statistic.StatisticFragment
import dev.kxxcn.woozoora.ui.direction.statistic.StatisticViewModel

@Module
abstract class StatisticModule {

    @ContributesAndroidInjector(
        modules = [ViewModelBuilder::class]
    )
    internal abstract fun fragment(): StatisticFragment

    @Binds
    @IntoMap
    @ViewModelKey(StatisticViewModel::class)
    abstract fun bindViewModel(factory: StatisticViewModel.Factory): AssistedSavedStateViewModelFactory<out ViewModel>
}
