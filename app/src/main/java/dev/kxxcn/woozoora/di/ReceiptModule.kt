package dev.kxxcn.woozoora.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.woozoora.ui.receipt.ReceiptFragment
import dev.kxxcn.woozoora.ui.receipt.ReceiptViewModel

@Module
abstract class ReceiptModule {

    @ContributesAndroidInjector(
        modules = [ViewModelBuilder::class]
    )
    internal abstract fun fragment(): ReceiptFragment

    @Binds
    @IntoMap
    @ViewModelKey(ReceiptViewModel::class)
    abstract fun bindViewModel(factory: ReceiptViewModel.Factory): AssistedSavedStateViewModelFactory<out ViewModel>
}
