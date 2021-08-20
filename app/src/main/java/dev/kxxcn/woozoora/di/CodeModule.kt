package dev.kxxcn.woozoora.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.woozoora.ui.code.CodeFragment
import dev.kxxcn.woozoora.ui.code.CodeViewModel

@Module
abstract class CodeModule {

    @ContributesAndroidInjector(
        modules = [ViewModelBuilder::class]
    )
    internal abstract fun fragment(): CodeFragment

    @Binds
    @IntoMap
    @ViewModelKey(CodeViewModel::class)
    abstract fun bindViewModel(viewModel: CodeViewModel): ViewModel
}
