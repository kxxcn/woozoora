package dev.kxxcn.woozoora.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import dev.kxxcn.woozoora.ui.input.InputFragment

@Module
abstract class InputModule {

    @ContributesAndroidInjector
    internal abstract fun fragment(): InputFragment
}
