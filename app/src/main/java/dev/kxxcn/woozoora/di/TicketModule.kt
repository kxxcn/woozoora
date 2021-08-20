package dev.kxxcn.woozoora.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.woozoora.ui.ticket.TicketFragment
import dev.kxxcn.woozoora.ui.ticket.TicketViewModel

@Module
abstract class TicketModule {

    @ContributesAndroidInjector(
        modules = [ViewModelBuilder::class]
    )
    internal abstract fun fragment(): TicketFragment

    @Binds
    @IntoMap
    @ViewModelKey(TicketViewModel::class)
    abstract fun bindViewModel(factory: TicketViewModel.Factory): AssistedSavedStateViewModelFactory<out ViewModel>
}
