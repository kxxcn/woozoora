package dev.kxxcn.woozoora.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.woozoora.ui.copy.CopyFragment
import dev.kxxcn.woozoora.ui.copy.CopyViewModel

@Module
abstract class CopyModule {

    @ContributesAndroidInjector(
        modules = [ViewModelBuilder::class]
    )
    internal abstract fun fragment(): CopyFragment

    @Binds
    @IntoMap
    @ViewModelKey(CopyViewModel::class)
    abstract fun bindViewModel(viewModel: CopyViewModel): ViewModel
}
