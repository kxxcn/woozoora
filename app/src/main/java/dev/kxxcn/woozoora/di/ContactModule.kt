package dev.kxxcn.woozoora.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.woozoora.ui.contact.ContactFragment
import dev.kxxcn.woozoora.ui.contact.ContactViewModel

@Module
abstract class ContactModule {

    @ContributesAndroidInjector(
        modules = [ViewModelBuilder::class]
    )
    internal abstract fun fragment(): ContactFragment

    @Binds
    @IntoMap
    @ViewModelKey(ContactViewModel::class)
    abstract fun bindViewModel(viewModel: ContactViewModel): ViewModel
}
