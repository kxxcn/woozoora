package dev.kxxcn.woozoora.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.woozoora.ui.invite.InviteFragment
import dev.kxxcn.woozoora.ui.invite.InviteViewModel

@Module
abstract class InviteModule {

    @ContributesAndroidInjector(
        modules = [ViewModelBuilder::class]
    )
    internal abstract fun fragment(): InviteFragment

    @Binds
    @IntoMap
    @ViewModelKey(InviteViewModel::class)
    abstract fun bindViewModel(viewModel: InviteViewModel): ViewModel
}
