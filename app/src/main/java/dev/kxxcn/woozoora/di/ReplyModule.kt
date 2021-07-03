package dev.kxxcn.woozoora.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.woozoora.ui.reply.ReplyFragment
import dev.kxxcn.woozoora.ui.reply.ReplyViewModel

@Module
abstract class ReplyModule {

    @ContributesAndroidInjector(
        modules = [ViewModelBuilder::class]
    )
    internal abstract fun fragment(): ReplyFragment

    @Binds
    @IntoMap
    @ViewModelKey(ReplyViewModel::class)
    abstract fun bindViewModel(viewModel: ReplyViewModel): ViewModel
}
