package dev.kxxcn.woozoora.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.woozoora.ui.ask.AskFragment
import dev.kxxcn.woozoora.ui.ask.AskViewModel

@Module
abstract class AskModule {

    @ContributesAndroidInjector(
        modules = [ViewModelBuilder::class]
    )
    internal abstract fun fragment(): AskFragment

    @Binds
    @IntoMap
    @ViewModelKey(AskViewModel::class)
    abstract fun bindViewModel(viewModel: AskViewModel): ViewModel
}
