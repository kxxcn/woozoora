package dev.kxxcn.woozoora.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.woozoora.ui.notification.NotificationFragment
import dev.kxxcn.woozoora.ui.notification.NotificationViewModel

@Module
abstract class NotificationModule {

    @ContributesAndroidInjector(
        modules = [ViewModelBuilder::class]
    )
    internal abstract fun fragment(): NotificationFragment

    @Binds
    @IntoMap
    @ViewModelKey(NotificationViewModel::class)
    abstract fun bindViewModel(viewModel: NotificationViewModel): ViewModel
}
