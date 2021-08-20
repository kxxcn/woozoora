package dev.kxxcn.woozoora.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import dev.kxxcn.woozoora.ui.contact.ContactFragment

@Module
abstract class ContactModule {

    @ContributesAndroidInjector(
        modules = [ViewModelBuilder::class]
    )
    internal abstract fun fragment(): ContactFragment
}
