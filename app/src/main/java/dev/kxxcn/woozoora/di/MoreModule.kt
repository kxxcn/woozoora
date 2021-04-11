package dev.kxxcn.woozoora.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.woozoora.ui.direction.more.MoreFragment
import dev.kxxcn.woozoora.ui.direction.more.MoreViewModel

@Module
abstract class MoreModule {

    @ContributesAndroidInjector(
        modules = [ViewModelBuilder::class]
    )
    internal abstract fun fragment(): MoreFragment

    @Binds
    @IntoMap
    @ViewModelKey(MoreViewModel::class)
    abstract fun bindViewModel(viewModel: MoreViewModel): ViewModel
}
