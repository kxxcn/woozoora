package dev.kxxcn.woozoora.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.woozoora.ui.invoice.InvoiceFragment
import dev.kxxcn.woozoora.ui.invoice.InvoiceViewModel

@Module
abstract class InvoiceModule {

    @ContributesAndroidInjector(
        modules = [ViewModelBuilder::class]
    )
    internal abstract fun fragment(): InvoiceFragment

    @Binds
    @IntoMap
    @ViewModelKey(InvoiceViewModel::class)
    abstract fun bindViewModel(viewModel: InvoiceViewModel): ViewModel
}
