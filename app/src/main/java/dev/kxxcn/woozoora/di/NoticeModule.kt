package dev.kxxcn.woozoora.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.kxxcn.woozoora.ui.notice.NoticeFragment
import dev.kxxcn.woozoora.ui.notice.NoticeViewModel

@Module
abstract class NoticeModule {

    @ContributesAndroidInjector(
        modules = [ViewModelBuilder::class]
    )
    internal abstract fun fragment(): NoticeFragment

    @Binds
    @IntoMap
    @ViewModelKey(NoticeViewModel::class)
    abstract fun bindViewModel(viewModel: NoticeViewModel): ViewModel
}
